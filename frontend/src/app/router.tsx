import MainPage from '../pages/main/main';
import SignInPage from '../pages/login/signIn';
import SignUpPage from '../pages/login/signUp';
import CoordiFromCoordiPage from '../pages/coordi/coordi-from-coordi';
import CoordiFromFeedPage from '../pages/coordi/coordi-from-feed';
import FeedPage from '../pages/feed/feed';
import FeedDetail from '../pages/feed/feedDetail';
import AnalysisPage from '../pages/analysis/analysis';
import ClosetPage from '../pages/closet/closet';
import RecommendationPage from '../pages/recommendation/recommendation';

import { BrowserRouter, Route, Routes } from 'react-router-dom';

//:part/:feedId/:clothId
const Router = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />}></Route>
        <Route path="/login" element={<SignInPage />}></Route>
        <Route path="/signup" element={<SignUpPage />}></Route>
        <Route path="/coordi/0" element={<CoordiFromCoordiPage />}></Route>
        <Route path="/coordi/1/:feedId" element={<CoordiFromFeedPage />}></Route>
        <Route path="/feed" element={<FeedPage />}></Route>
        <Route path="/feeddetail/:feedId" element={<FeedDetail />}></Route>
        <Route path="/analysis/*" element={<AnalysisPage />}>
          <Route path="closet" element={<ClosetPage />}></Route>
          <Route path="recommendation" element={<RecommendationPage />}></Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Router;
