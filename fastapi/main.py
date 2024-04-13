import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

current_path = os.getcwd()

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
sys.path.append(current_path + '/utility')

print(sys.path)

import shutil

from fastapi import FastAPI, UploadFile

import torch
import torchvision.transforms as transforms
from utility.resnest import resnest50d
from PIL import Image

app = FastAPI()


UPLOAD_DIRECTORY = current_path + "/uploads"

print(UPLOAD_DIRECTORY)

category = ["재킷", "조거팬츠", "짚업", "스커트", "가디건", "점퍼", "티셔츠", "셔츠", "팬츠", "드레스", "패딩", "청바지", "점프수트", "니트웨어", "베스트", "코트", "브라탑", "블라우스", "탑", "후드티", "래깅스"]

detail = ['스터드', '드롭숄더', '드롭웨이스트', '레이스업', '슬릿',
          '프릴', '단추', '퀄팅', '스팽글', '롤업',
          '니트꽈베기', '체인', '프린지', '지퍼', '태슬',
          '띠', '플레어', '싱글브레스티드', '더블브레스티드', '스트링',
          '자수', '폼폼', '디스트로이드', '페플럼', 'X스트랩',
          '스티치', '레이스', '퍼프', '비즈', '컷아웃',
          '버클', '포켓', '러플', '글리터', '퍼트리밍',
          '플리츠', '비대칭', '셔링', '패치워크', '리본']

texture = ['패딩', '무스탕', '퍼프', '네오프렌', '코듀로이',
           '트위드', '자카드', '니트', '페플럼', '레이스',
           '스판덱스', '메시', '비닐/PVC', '데님', '울/캐시미어',
           '저지', '시퀸/글리터', '퍼', '헤어 니트', '실크',
           '린넨', '플리스', '시폰', '스웨이드', '가죽', '우븐', '벨벳']

@app.get("/")
async def root():
    return {"message": "FastAPI Connection Test"}


@app.post("/closet")
async def closet_attribute_classifier(file: UploadFile):
    image_path = upload_image(file)
    image = Image.open(image_path)

    preprocess = transforms.Compose([
        transforms.Resize(224),
        transforms.ToTensor(),
        transforms.Normalize(
            mean=[0.485, 0.456, 0.406],
            std=[0.229, 0.224, 0.225]
        ),
    ])

    image = image.convert("RGB")
    input_image = preprocess(image).unsqueeze(0)  # 배치 차원 추가

    category_model = load_category_model()
    detail_model = load_detail_model()
    texture_model = load_texture_model()

    result = dict()

    category_classes = classification(category_model, input_image, category)
    detail_classes = classification(detail_model, input_image, detail)
    texture_classes = classification(texture_model, input_image, texture)

    result["category"] = category_classes
    result["detail"] = detail_classes
    result["texture"] = texture_classes

    return result


def upload_image(file: UploadFile):
    # 업로드된 파일을 저장할 경로 설정
    upload_path = os.path.join(UPLOAD_DIRECTORY, file.filename)

    # 업로드된 파일을 디스크에 저장
    with open(upload_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    return upload_path


def load_category_model():
    path = 'checkpoint/model_category_best.pth.tar'

    model = resnest50d(pretrained=False, nc=21)
    pretrained = torch.load(path, map_location=torch.device('cpu'))

    state = pretrained['state_dict']
    model.load_state_dict(state_dict=state, strict=False)

    return model


def load_detail_model():
    path = 'checkpoint/model_detail_best.pth.tar'

    model = resnest50d(pretrained=False, nc=40)
    pretrained = torch.load(path, map_location=torch.device('cpu'))

    state = pretrained['state_dict']
    model.load_state_dict(state_dict=state, strict=False)

    return model


def load_texture_model():
    path = 'checkpoint/model_texture_best.pth.tar'

    model = resnest50d(pretrained=False, nc=27)
    pretrained = torch.load(path, map_location=torch.device('cpu'))

    state = pretrained['state_dict']
    model.load_state_dict(state_dict=state, strict=False)

    return model


def classification(model, input_image, class_type):
    model.eval()

    # 모델에 입력 이미지 제공
    output = model(input_image)

    probabilities = torch.nn.functional.softmax(output[0], dim=0)
    top5_prob, top5_indices = torch.topk(probabilities, 5)

    classes = list()

    for rank in range(5):
        probability = top5_prob[rank].item()

        if probability > 0.3:
            attribute = class_type[top5_indices[rank]]
            print(probability, attribute)
            classes.append(attribute)
            
    predicted_labels = torch.argmax(output, dim=1)

    if not len(classes):
        classes.append(class_type[predicted_labels.item()])

    return classes