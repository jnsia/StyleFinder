import { useEffect, useState } from 'react';
import RecommendationItem from './recommendation-Item';
import Dropbox from '../../shared/ui/dropbox/dropbox';

import api from '../../entities/analysis/analysis-apis';

import { useNavigate } from 'react-router';
import { RecommendCloth } from '../../entities/recommend/recommend-types';
import { axiosError } from '../../shared/utils/axiosError';
import useLoginStore from '../../shared/store/use-login-store';
import useClothStore from '../../shared/store/use-cloth-store';

const RecommendationTastes = () => {
  const navigate = useNavigate();
  const loginStore = useLoginStore();
  const clothStore = useClothStore();
  const [taste, setTaste] = useState<string>('');
  const tasteList = [
    '',
    '레트로',
    '로맨틱',
    '리조트',
    '매니시',
    '모던',
    '밀리터리',
    '섹시',
    '소피스트케이티드',
    '스트리트',
    '스포티',
    '아방가르드',
    '오리엔탈',
    '웨스턴',
    '젠더리스',
    '컨트리',
    '클래식',
    '키치',
    '톰보이',
    '펑크',
    '페미닌',
    '프레피',
    '히피',
    '힙합',
  ];

  const [tasteResponseList, setTasteResponseList] = useState<RecommendCloth[]>([]);

  // 취향 설정
  const handleSelectedTaste = (selectedItem: string) => {
    setTaste(selectedItem);
  };

  // 해당 taste 에 대한 추천 결과 리스트를 조회
  const handleGetTasteList = () => {
    api
      .recommendByStyle(taste)
      .then((response) => {
        const data = response.data;

        setTasteResponseList(data);
        console.log(data);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  // 해당 아이템 코디 해 보기
  const handleClickMoveToCoordi = (selectedItem: RecommendCloth) => {
    clothStore.createCloth(selectedItem);
    navigate(`/coordi/0`);
  };

  useEffect(() => {
    handleGetTasteList();
  }, [taste]);

  return (
    <div className="py-4 my-4">
      <div className="flex justify-between">
        <div className="text-lg">취향별 추천</div>
        <Dropbox options={tasteList} onSelected={(select) => handleSelectedTaste(select)}></Dropbox>
      </div>
      {tasteResponseList.length == 0 ? (
        <div className="mx-4 my-32">
          <div className="my-20 text-center">검색된 추천 리스트가 없습니다!</div>
        </div>
      ) : (
        <div className="mx-4 my-2">
          <div className="flex w-full h-auto pt-3 overflow-x-scroll pl-7 scrollbar-hide">
            {tasteResponseList.map((item, index) => (
              <div className="mr-5">
                <RecommendationItem key={index} item={item} onClickItem={() => handleClickMoveToCoordi(item)} />
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default RecommendationTastes;
