import CustomButton from '../../shared/ui/button/custom-button';
import { ClosetCloth } from '../../entities/closet/closet-types';
import WhiteButton from '../../shared/ui/button/white-button';
import { useState } from 'react';

interface MyClosetItemProps {
  item: ClosetCloth;
  onClickItem(item: ClosetCloth): void;
  onClickDeleteItem(item: ClosetCloth): void;
}

const MyClosetItem = (props: MyClosetItemProps) => {
  const [isOverlayVisible, setIsOverlayVisible] = useState(false);

  // Hover 로 상세 정보 확인
  const handleMouseLeave = () => {
    setIsOverlayVisible(false);
  };

  const handleMouseEnter = () => {
    setIsOverlayVisible(true);
  };

  return (
    <div className="my-2">
      <div className="relative">
        <img className="w-64 h-64 m-2 border-2 rounded-md max-h-64" src={`data:image/png;base64,${props.item.image}`} onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}></img>
        {isOverlayVisible && (
          <div className="absolute inset-0 w-64 h-auto ml-2 bg-black border-2 rounded-md max-h-64 opacity-90">
            <div className="absolute inset-0 flex flex-col items-center justify-center text-white">
              {props.item.part && <p>착용 부위: {props.item.part}</p>}
              {props.item.categories && <p>카테고리: {props.item.categories}</p>}
              {props.item.details && <p>디테일: {props.item.details}</p>}
              {props.item.textures && <p>소재: {props.item.textures}</p>}
            </div>
          </div>
        )}
      </div>
      <div className="flex m-2">
        <CustomButton className="w-40 h-12" value="코디 해 보기" onClick={() => props.onClickItem(props.item)} />
        <WhiteButton className="w-24 h-12 border-2" value="휴지통" onClick={() => props.onClickDeleteItem(props.item)} />
      </div>
    </div>
  );
};

export default MyClosetItem;
