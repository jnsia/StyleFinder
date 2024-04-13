import { useEffect, useState } from 'react';
import RecommendationItem from './recommendation-Item';
import Dropbox from '../../shared/ui/dropbox/dropbox';

import { useNavigate } from 'react-router';
import { RecommendCloth } from '../../entities/recommend/recommend-types';
import useLoginStore from '../../shared/store/use-login-store';
import { axiosError } from '../../shared/utils/axiosError';
import api from '../../entities/analysis/analysis-apis';
import useClothStore from '../../shared/store/use-cloth-store';

const RecommendationCategories = () => {
  const navigate = useNavigate();
  const clothStore = useClothStore();
  const loginStore = useLoginStore();
  const [category, setCategory] = useState<string>('');
  // 하위 카테고리 추가 필요
  const categoryList = [
    '',
    '재킷',
    '조거팬츠',
    '짚업',
    '스커트',
    '가디건',
    '점퍼',
    '티셔츠',
    '셔츠',
    '팬츠',
    '드레스',
    '패딩',
    '청바지',
    '점프수트',
    '니트웨어',
    '베스트',
    '코트',
    '브라탑',
    '블라우스',
    '탑',
    '후드티',
    '래깅스',
  ];
  const [categoryResponseList, setCategoryResponseList] = useState<RecommendCloth[]>([]);

  // 카테고리 설정
  const handleSelectedCategory = (selectedItem: string) => {
    setCategory(selectedItem);
  };

  // 해당 category 에 대한 추천 결과 리스트를 조회
  const handleGetCategoryList = () => {
    api
      .recommendByCategory(category)
      .then((response) => {
        const data = response.data;

        setCategoryResponseList(data);
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
    if (category) {
      handleGetCategoryList();
    }
  }, [category]);

  return (
    <div className="py-4 my-4">
      <div className="flex justify-between">
        <div className="text-lg">카테고리별 추천</div>
        <Dropbox options={categoryList} onSelected={(select) => handleSelectedCategory(select)}></Dropbox>
      </div>
      {categoryResponseList.length == 0 ? (
        <div className="mx-4 my-32">
          <div className="my-20 text-center">검색된 추천 리스트가 없습니다!</div>
        </div>
      ) : (
        <div className="mx-4 my-2">
          <div className="flex w-full h-auto pt-3 overflow-x-scroll pl-7 scrollbar-hide">
            {categoryResponseList.map((item, index) => (
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

export default RecommendationCategories;
