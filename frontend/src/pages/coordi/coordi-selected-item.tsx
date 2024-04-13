import { useState } from 'react';
import { RecommendCloth } from '../../entities/recommend/recommend-types';

import NoImage from '../../assets/images/noimage.png';

interface CoordiItemProps {
  item: RecommendCloth | null;
  id: string;
}

const CoordiSelectedItem = (props: CoordiItemProps) => {
  const [isOverlayVisible, setIsOverlayVisible] = useState(false);

  // Hover 로 상세 정보 확인
  const handleMouseLeave = () => {
    setIsOverlayVisible(false);
  };

  const handleMouseEnter = () => {
    setIsOverlayVisible(true);
  };

  return (
    <div className="my-8">
      <div className="relative">
        {props.item != null ? (
          <div>
            <img
              id={props.id}
              className="w-64 h-64 m-2 border-2 rounded-md max-h-64"
              src={`data:image/png;base64,${props.item.image}`}
              onMouseEnter={handleMouseEnter}
              onMouseLeave={handleMouseLeave}
            ></img>
            {isOverlayVisible && (
              <div className="absolute inset-0 w-64 h-auto ml-2 bg-black border-2 rounded-md max-h-64 opacity-90">
                <div className="absolute inset-0 flex flex-col items-center justify-center text-white">
                  {props.item.part && <p>착용 부위: {props.item.part}</p>}
                  {props.item.category && <p>카테고리: {props.item.category}</p>}
                  {props.item.style && <p>스타일: {props.item.style}</p>}
                  {props.item.color && <p>색상: {props.item.color}</p>}
                </div>
              </div>
            )}
          </div>
        ) : (
          <img id={props.id} className="w-64 h-auto border-2 rounded-md max-h-64" src={NoImage} />
        )}
      </div>
    </div>
  );
};

export default CoordiSelectedItem;
