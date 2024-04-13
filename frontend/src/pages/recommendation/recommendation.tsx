// import useLoginStore from '../../shared/store/useLoginStore';
// import useUserStore from '../../shared/store/useUserStore';

// import { Link } from 'react-router-dom';

import RecommendTastes from './recommend-tastes';
import RecommendColors from './recommend-colors';
import RecommendCategories from './recommend-categories';
import Keywords from '../../features/analysis/kewords';

const Recommendation = () => {
  // const loginStore = useLoginStore();
  // const userStore = useUserStore();

  // // 아이템 선택 시 해당 아이템으로 코디 해 보기
  // const handleClickMoveToCoordi = () => {};

  return (
    <div className="mx-auto px-36">
      <div className="pt-20">
        <div className="content-center">
          <Keywords />
        </div>
        <div className="pt-4 mt-4">
          <div className="content-center">
            <RecommendTastes />
          </div>

          <div className="content-center">
            <RecommendCategories />
          </div>

          <div className="content-center">
            <RecommendColors />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Recommendation;
