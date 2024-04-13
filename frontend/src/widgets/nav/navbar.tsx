/* eslint-disable @typescript-eslint/no-unused-vars */
import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react'; // useEffect 추가
import useLoginStore from '../../shared/store/use-login-store';
import logo from '../../assets/logos/logo.png';
import './nav.css';

const Navbar = () => {
  const loginStore = useLoginStore();
  const [userNickname, setUserNickname] = useState('');

  const handleLogout = () => {
    // 로그아웃 버튼 클릭 시 로그아웃 처리
    loginStore.setLogout();
    localStorage.removeItem('isLoggedIn');
    setUserNickname(''); // 사용자 닉네임 초기화
  };

  // 컴포넌트가 마운트될 때 로컬 스토리지에서 사용자 정보를 가져옴
  useEffect(() => {
    userNickname;

    const storedUserNickname = localStorage.getItem('userNickname');
    if (storedUserNickname) {
      setUserNickname(storedUserNickname);
    }
  }, []);

  return (
    <div className="navbar flex flex-row h-20 bg-[#161A30]">
      <div className="flex items-center justify-center w-48 m-5 rounded-full navbar-start">
        <div className="flex items-center logo">
          <Link to="/" replace={true}>
            <img className="w-10 h-10" src={logo} alt="" />
          </Link>
          <Link to="/" replace={true} className="ml-2 nav-text">
            StyleFinder
          </Link>
        </div>
      </div>

      <div className="flex items-center justify-end flex-grow mt-2">
        <div className="flex justify-between p-2 m-5 mt-3 rounded-full menu">
          <button className="w-20 h-10">
            <Link to="/coordi/0" replace={true}>
              코디
            </Link>
          </button>
          <button className="w-20 h-10">
            <Link to="/feed" replace={true}>
              피드
            </Link>
          </button>
          <button className="w-20 h-10">
            <Link to="/analysis/closet" replace={true}>
              분석
            </Link>
          </button>
        </div>
      </div>

      <div className="flex items-center justify-center w-20 m-5 text-center rounded-full navbar-end menu">
        {!loginStore.isLogin ? (
          <button>
            <Link to="/login" replace={true} className="mr-2">
              로그인
            </Link>
          </button>
        ) : (
          <div className="flex items-center justify-center">
            <button onClick={handleLogout}>로그아웃</button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Navbar;
