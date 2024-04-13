/* eslint-disable no-useless-escape */
/* eslint-disable @typescript-eslint/no-explicit-any */
import Navbar from '../../widgets/nav/navbar';

import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import useLoginStore from '../../shared/store/use-login-store';
import api from '../../entities/user/user-apis';
import CustomButton from '../../shared/ui/button/custom-button';
//import useUserStore from '../shared/store/useUserStore';

const SignIn = () => {
  const navigate = useNavigate();
  const loginStore = useLoginStore();
  //const userStore = useUserStore();

  const [email, setEmail] = useState('');
  const [pw, setPw] = useState('');
  const [emailValid, setEmailValid] = useState(false);
  const [pwValid, setPwValid] = useState(false);
  const [notAllow, setNotAllow] = useState(true);

  const handleEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
    const regex = /^(([^<>()\[\].,;:\s@"]+(\.[^<>()\[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;
    setEmailValid(regex.test(email));
  };

  const handlePw = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newPassword = e.target.value;
    setPw(newPassword);
    const regex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,}$/;
    setPwValid(regex.test(newPassword));
  };

  const onClickConfirmButton = () => {
    const request = {
      email: email,
      password: pw,
    };

    api
      .signIn(request)
      .then((response) => {
        localStorage.setItem('userInfo', JSON.stringify(response.data));
        localStorage.setItem('isLoggedIn', 'true');
        loginStore.setLogin();
        alert('로그인 성공');
        navigate('/');
      })
      .catch((error: any) => {
        const errorCode = error.response.status;
        const errorMessage = error.response.data.message;
        console.log(error);
        console.log(errorCode, errorMessage);
        alert(errorMessage);
      });
  };

  useEffect(() => {
    setNotAllow(!(emailValid && pwValid));
  }, [emailValid, pwValid]);

  return (
    <>
      <Navbar></Navbar>
      <div className="flex justify-center mt-3">
        {!loginStore.isLogin ? (
          <div className="box-border flex justify-center w-2/3 p-4 border-4 h-72">
            <div className="contentWrap">
              <div className="mt-5 inputTitle">이메일 주소</div>
              <div className="inputWrap">
                <input className="input" placeholder="이메일 입력" value={email} onChange={handleEmail} />
              </div>
              <div className="errorMessageWrap text-slate-400">{!emailValid && email.length > 0 && <div>올바른 이메일을 입력해 주세요.</div>}</div>
              <div className="mt-2 inputTitle">비밀번호</div>
              <div className="inputWrap">
                <input type="password" className="input" placeholder="비밀번호 입력" value={pw} onChange={handlePw} />
              </div>
              <div className="errorMessageWrap text-slate-400">{!pwValid && pw.length > 0 && <div>올바른 비밀번호를 입력해주세요.</div>}</div>
              <div className="flex justify-between mt-4 mb-2">
                <Link to="/signup">
                  <CustomButton onClick={() => {}} value="회원 가입" />
                </Link>
                <CustomButton onClick={onClickConfirmButton} disabled={notAllow} className="bottomButton" value="로그인" />
              </div>
            </div>
          </div>
        ) : (
          <div>
            <Link to="/feed">
              <CustomButton onClick={() => {}} value="피드로 이동하기" />
            </Link>
          </div>
        )}
      </div>
    </>
  );
};
export default SignIn;
