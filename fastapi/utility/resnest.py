from torch import nn
from .helpers import build_model_with_cfg
from .resnet import ResNet
from .layers import SplitAttnConv2d

DEFAULT_CROP_PCT = 0.875
IMAGENET_DEFAULT_MEAN = (0.485, 0.456, 0.406)
IMAGENET_DEFAULT_STD = (0.229, 0.224, 0.225)
IMAGENET_INCEPTION_MEAN = (0.5, 0.5, 0.5)
IMAGENET_INCEPTION_STD = (0.5, 0.5, 0.5)
IMAGENET_DPN_MEAN = (124 / 255, 117 / 255, 104 / 255)
IMAGENET_DPN_STD = tuple([1 / (.0167 * 255)] * 3)


def _cfg(url='', **kwargs):
    return {
        'url': url,
        'num_classes': 21, 'input_size': (3, 224, 224), 'pool_size': (7, 7),
        'crop_pct': 0.875, 'interpolation': 'bilinear',
        'mean': IMAGENET_DEFAULT_MEAN, 'std': IMAGENET_DEFAULT_STD,
        'first_conv': 'conv1.0', 'classifier': 'fc',
        **kwargs
    }


default_cfgs = {
    'resnest50d': _cfg(
        url='https://github.com/rwightman/pytorch-image-models/releases/download/v0.1-resnest/resnest50-528c19ca.pth'),
}


class ResNestBottleneck(nn.Module):
    """ResNet Bottleneck
    """
    # pylint: disable=unused-argument
    expansion = 4

    def __init__(self, inplanes, planes, stride=1, downsample=None,
                 radix=1, cardinality=1, base_width=64, avd=False, avd_first=False, is_first=False,
                 reduce_first=1, dilation=1, first_dilation=None, act_layer=nn.ReLU, norm_layer=nn.BatchNorm2d,
                 attn_layer=None, aa_layer=None, drop_block=None, drop_path=None):
        super(ResNestBottleneck, self).__init__()
        assert reduce_first == 1  # not supported
        assert attn_layer is None  # not supported
        assert aa_layer is None  # TODO not yet supported
        assert drop_path is None  # TODO not yet supported

        group_width = int(planes * (base_width / 64.)) * cardinality
        first_dilation = first_dilation or dilation
        if avd and (stride > 1 or is_first):
            avd_stride = stride
            stride = 1
        else:
            avd_stride = 0
        self.radix = radix
        self.drop_block = drop_block

        self.conv1 = nn.Conv2d(inplanes, group_width, kernel_size=1, bias=False)
        self.bn1 = norm_layer(group_width)
        self.act1 = act_layer(inplace=True)
        self.avd_first = nn.AvgPool2d(3, avd_stride, padding=1) if avd_stride > 0 and avd_first else None

        if self.radix >= 1:
            self.conv2 = SplitAttnConv2d(
                group_width, group_width, kernel_size=3, stride=stride, padding=first_dilation,
                dilation=first_dilation, groups=cardinality, radix=radix, norm_layer=norm_layer, drop_block=drop_block)
            self.bn2 = None  # FIXME revisit, here to satisfy current torchscript fussyness
            self.act2 = None
        else:
            self.conv2 = nn.Conv2d(
                group_width, group_width, kernel_size=3, stride=stride, padding=first_dilation,
                dilation=first_dilation, groups=cardinality, bias=False)
            self.bn2 = norm_layer(group_width)
            self.act2 = act_layer(inplace=True)
        self.avd_last = nn.AvgPool2d(3, avd_stride, padding=1) if avd_stride > 0 and not avd_first else None

        self.conv3 = nn.Conv2d(group_width, planes * 4, kernel_size=1, bias=False)
        self.bn3 = norm_layer(planes*4)
        self.act3 = act_layer(inplace=True)
        self.downsample = downsample

    def zero_init_last_bn(self):
        nn.init.zeros_(self.bn3.weight)

    def forward(self, x):
        residual = x

        out = self.conv1(x)
        out = self.bn1(out)
        if self.drop_block is not None:
            out = self.drop_block(out)
        out = self.act1(out)

        if self.avd_first is not None:
            out = self.avd_first(out)

        out = self.conv2(out)
        if self.bn2 is not None:
            out = self.bn2(out)
            if self.drop_block is not None:
                out = self.drop_block(out)
            out = self.act2(out)

        if self.avd_last is not None:
            out = self.avd_last(out)

        out = self.conv3(out)
        out = self.bn3(out)
        if self.drop_block is not None:
            out = self.drop_block(out)

        if self.downsample is not None:
            residual = self.downsample(x)

        out += residual
        out = self.act3(out)
        return out


def _create_resnest(variant, pretrained=False, **kwargs):
    return build_model_with_cfg(
        ResNet, variant, default_cfg=default_cfgs[variant], pretrained=pretrained, **kwargs)

def resnest50d(pretrained=False, **kwargs):
    """ ResNeSt-50d model. Matches paper ResNeSt-50 model, https://arxiv.org/abs/2004.08955
    Since this codebase supports all possible variations, 'd' for deep stem, stem_width 32, avg in downsample.
    """
    model_kwargs = dict(
        block=ResNestBottleneck, layers=[3, 4, 6, 3],
        stem_type='deep', stem_width=32, avg_down=True, base_width=64, cardinality=1,
        block_args=dict(radix=2, avd=True, avd_first=False), **kwargs)
    return _create_resnest('resnest50d', pretrained=pretrained, **model_kwargs)