/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable prefer-const */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unused-vars */
import { useEffect, useRef, useState } from 'react';
import api from '../../entities/user/user-apis';
import { axiosError } from '../../shared/utils/axiosError';
import { UserInfo } from '../../entities/user/user-types';
import useLoginStore from '../../shared/store/use-login-store';
import { useNavigate } from 'react-router';
import './my.css';
import CustomButton from '../../shared/ui/button/custom-button';
import WhiteButton from '../../shared/ui/button/white-button';

const style = [
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

const categories = [
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

const My = () => {
  const loginStore = useLoginStore();
  const navigate = useNavigate();

  const modalBackground = useRef(null);

  const [userInfo, setUserInfo] = useState<UserInfo>();

  const [isUpdate, setIsUpdate] = useState(false);

  const [height, setHeight] = useState(0);
  const [weight, setWeight] = useState(0);
  const [nickname, setNickname] = useState('');
  const [image, setImage] = useState<File | null>(null);
  const [introduce, setIntroduce] = useState('');
  const [dislikeCategories] = useState<string[]>([]);

  const [heightValid, setHeightValid] = useState(true);
  const [weightValid, setWeightValid] = useState(true);
  const [notAllow, setNotAllow] = useState(true);

  const [feedContent, setFeedContent] = useState('');

  const [selectedOptions, setSelectedOptions] = useState<string[]>([]);

  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const value = e.target.value;
    if (!selectedOptions.includes(value)) {
      setSelectedOptions([...selectedOptions, value]);
    }
  };

  const handleOptionRemove = (option: string) => {
    setSelectedOptions(selectedOptions.filter((item) => item !== option));
  };

  useEffect(() => {
    setNotAllow(!(heightValid && weightValid && nickname));
  }, [heightValid, weightValid, nickname]);

  const validateNumber = (value: string) => {
    return /^[0-9]*$/.test(value) && value.length > 0;
  };

  const handleHeight = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setHeight(Number(value));
    setHeightValid(validateNumber(value));
  };

  const handleWeight = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setWeight(Number(value));
    setWeightValid(validateNumber(value));
  };

  const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setImage(file);
    }
  };

  const modifyUserInfo = () => {
    if (notAllow) return;

    const request: any = new FormData();

    const profileImage = image;
    const updateUserInfoRequest = new Blob(
      [
        JSON.stringify({
          nickname: nickname,
          height: height,
          weight: weight,
          introduce: feedContent, // 수정된 부분: feedContent state를 사용하여 피드 내용 업데이트
          likeCategories: selectedOptions,
          dislikeCategories: dislikeCategories,
        }),
      ],
      { type: 'application/json' },
    );

    request.append('profileImage', profileImage);
    request.append('updateUserInfoRequest', updateUserInfoRequest);

    api
      .updateUserInfo(request)
      .then(() => {
        setIsUpdate(false);
        getUserInfo();
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  const getUserInfo = () => {
    api
      .getUserInfo()
      .then((response) => {
        const data: UserInfo = response.data.data;
        setUserInfo(data);

        setHeight(data.height);
        setWeight(data.weight);
        setNickname(data.nickname);
        setIntroduce(data.introduce);

        let likeCategories: string[] = [];

        data.likeCategories.forEach((category) => {
          if (category !== '') {
            likeCategories.push(category);
          }
        });

        setSelectedOptions(likeCategories);
      })
      .catch((error: any) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  useEffect(() => {
    getUserInfo();
    categories;
    style;
    introduce;
  }, []);

  useEffect(() => {
    if (userInfo) {
      setFeedContent(userInfo.introduce); // userInfo.introduce 값을 feedContent state의 초기값으로 설정
    }
  }, [userInfo]);

  return (
    <div>
      <div className="flex flex-row justify-between">
        <div className="pb-3 text-lg font-bold">내 정보</div>
        <div className="flex justify-center my-2">
          <WhiteButton onClick={() => setIsUpdate(true)} value="수정" />
        </div>
      </div>
      <div className="w-auto h-56 card bg-base-100 bg-[#F0ECE5] rounded-lg ">
        <div className="card-body">
          <div className="flex flex-row p-5 pl-8">
            <div>
              <div className="h-auto w-44">{userInfo?.profileImage && <img src={`data:image/png;base64,${userInfo?.profileImage}`} className="rounded-lg h-44" />}</div>
            </div>
            <div className="flex flex-col ml-4">
              <div className="pb-3 text-lg">{userInfo?.nickname}</div>
              <div className="pb-2 text-lg">
                <div className="text-gray-700">
                  {userInfo?.height} cm, {userInfo?.weight} kg
                </div>
              </div>
              <div>한 줄 소개</div>
              <div className="pb-2 text-gray-700">{userInfo?.introduce}</div>
              <div className="text-md">{userInfo?.likeCategories.map((category, index) => <WhiteButton key={index} className="rounded-full" value={category} />)}</div>
              {isUpdate && (
                <div
                  className={'modal-container'}
                  ref={modalBackground}
                  onClick={(e) => {
                    if (e.target === modalBackground.current) {
                      setIsUpdate(false);
                    }
                  }}
                >
                  <div className="flex justify-center w-1/2 h-auto bg-white">
                    <div>
                      {userInfo && (
                        <div>
                          <div className="flex justify-center pt-3 mb-5">
                            <img src={image ? URL.createObjectURL(image) : `data:image/png;base64,${userInfo.profileImage}`} alt="Profile Image" className="rounded-md uploadedImage" />
                          </div>
                          <div className="flex justify-center mb-5 inputWrap customInputWrap">
                            <label htmlFor="customFileInput" className="customFileInputLabel">
                              프로필 사진 업로드
                            </label>
                            <input type="file" id="customFileInput" accept="image/*" onChange={handleImageUpload} className="customFileInput" style={{ display: 'none' }} />
                          </div>
                        </div>
                      )}
                      <div className="flex justify-center pt-1 mb-5 inputWrap textdetail">
                        <input className="text-center input" placeholder="닉네임 입력" value={nickname} onChange={(e) => setNickname(e.target.value)} />
                      </div>
                      <div className="flex flex-row">
                        <div>
                          <div className="flex flex-row">
                            <div>
                              <div className="mb-5 mr-5 inputWrap">
                                <input className="w-16 mr-2 input" placeholder="키 입력" value={height} onChange={handleHeight} />
                                cm
                              </div>
                            </div>
                            <div>
                              <div className="inputWrap">
                                <input className="w-24 mr-2 input" placeholder="몸무게 입력" value={weight} onChange={handleWeight} />
                                kg
                              </div>
                            </div>
                          </div>
                          <div className="mb-5 inputWrap">
                            <textarea className="input" placeholder="피드 내용 입력" value={feedContent} onChange={(e) => setFeedContent(e.target.value)} />
                          </div>
                        </div>

                        <div className="flex flex-col pl-8">
                          <div>
                            <select className="w-full max-w-xs select select-bordered" onChange={handleSelectChange}>
                              <option disabled selected>
                                당신의 취향을 골라주세요
                              </option>
                              <option value="재킷">재킷</option>
                              <option value="조거팬츠">조거팬츠</option>
                              <option value="짚업">짚업</option>
                              <option value="스커트">스커트</option>
                              <option value="가디건">가디건</option>
                              <option value="점퍼">점퍼</option>
                              <option value="티셔츠">티셔츠</option>
                              <option value="셔츠">셔츠</option>
                              <option value="팬츠">팬츠</option>
                              <option value="드레스">드레스</option>
                              <option value="패딩">패딩</option>
                              <option value="청바지">청바지</option>
                              <option value="점프수트">점프수트</option>
                              <option value="니트웨어">니트웨어</option>
                              <option value="베스트">베스트</option>
                              <option value="코트">코트</option>
                              <option value="브라탑">브라탑</option>
                              <option value="블라우스">블라우스</option>
                              <option value="탑">탑</option>
                              <option value="후드티">후드티</option>
                              <option value="래깅스">래깅스</option>
                            </select>
                          </div>
                          <div>
                            {/* 선택된 옵션들 표시 */}
                            {selectedOptions.length > 0 && (
                              <div>
                                <p className="flex justify-center mt-3">선택된 옵션들</p>
                                {selectedOptions
                                  .reduce((rows: string[][], option, index) => {
                                    if (index % 2 === 0) rows.push([] as string[]);
                                    rows[rows.length - 1].push(option);
                                    return rows;
                                  }, [])
                                  .map((row, rowIndex) => (
                                    <div className="flex justify-between option-box-container" key={rowIndex}>
                                      {row.map((option) => (
                                        <div className="option-box" key={option}>
                                          <span>{option}</span>
                                          <button className="option-button custom-button" onClick={() => handleOptionRemove(option)}>
                                            취소
                                          </button>
                                        </div>
                                      ))}
                                    </div>
                                  ))}
                              </div>
                            )}
                          </div>
                        </div>
                        <div></div>
                      </div>
                      <div className="flex justify-center">
                        <CustomButton className="p-2 m-2 border-2 save" onClick={modifyUserInfo} value="저장" />
                        <WhiteButton className="p-2 m-2 border-2 cancel" onClick={() => setIsUpdate(false)} value="취소" />
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default My;
