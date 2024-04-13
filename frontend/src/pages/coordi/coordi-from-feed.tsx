/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import Navbar from '../../widgets/nav/navbar';

import { useEffect, useState } from 'react';
import './coordi.css';

import useOpenModal from '../../shared/hooks/use-open-modal';
import Modal from '../../shared/ui/modal/Modal';
import MyClosetReadModal from '../closet/my-closet-read-form';
import CustomButton from '../../shared/ui/button/custom-button';

import { RecommendCloth } from '../../entities/recommend/recommend-types';
import CustomTextArea from '../../shared/ui/input/custom-textarea';
import Input from '../../shared/ui/input/input';
import WhiteButton from '../../shared/ui/button/white-button';
import api from '../../entities/recommend/recommend-apis';
import { SearchFilter } from '../../entities/recommend/recommend-types';
import { axiosError } from '../../shared/utils/axiosError';
import useLoginStore from '../../shared/store/use-login-store';
import { useNavigate, useParams } from 'react-router';

import feedApi from '../../entities/feed/feed-apis';
import useClothStore from '../../shared/store/use-cloth-store';
import CoordiItem from './coordi-Item';
import CoordiSelectedItem from './coordi-selected-item';

const CoordiFromFeed = () => {
  const loginStore = useLoginStore();
  const clothStore = useClothStore();
  const navigate = useNavigate();

  const { feedId } = useParams<{ feedId: string }>();
  const { isOpenModal, clickModal, closeModal } = useOpenModal();

  const [title, setTitle] = useState<string>('');
  const [content, setContent] = useState<string>('');

  const [outerCloth, setOuterCloth] = useState<RecommendCloth | null>(null);
  const [upperBody, setUpperBody] = useState<RecommendCloth | null>(null);
  const [lowerBody, setLowerBody] = useState<RecommendCloth | null>(null);
  const [dress, setDress] = useState<RecommendCloth | null>(null);

  const [outerClothes, setOuterClothes] = useState<RecommendCloth[]>([]);
  const [upperBodys, setUpperBodys] = useState<RecommendCloth[]>([]);
  const [lowerBodys, setLowerBodys] = useState<RecommendCloth[]>([]);
  const [dresses, setDresses] = useState<RecommendCloth[]>([]);

  const [isSearchVisible, setIsSearchVisible] = useState(false);
  const [isRecommendListVisible, setIsRecommendListVisible] = useState(false);

  const [selectedStyles, setSelectedStyles] = useState<string[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [selectedColors, setSelectedColors] = useState<string[]>([]);

  const styles = [
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

  const colors = [
    '화이트',
    '그레이',
    '베이지',
    '라벤더',
    '오렌지',
    '블루',
    '와인',
    '블랙',
    '레드',
    '브라운',
    '스카이블루',
    '옐로우',
    '핑크',
    '실버',
    '네온',
    '퍼플',
    '카키',
    '민트',
    '그린',
    '골드',
    '네이비',
  ];

  // 부위별 아이템 선택 시 이미지 변경
  const handleClickItem = (newItem: RecommendCloth) => {
    switch (newItem.part) {
      case 'outerCloth':
      case 'outer':
        setOuterCloth(newItem);
        break;
      case 'upperBody':
      case 'upper':
        setUpperBody(newItem);
        break;
      case 'lowerBody':
      case 'lower':
        setLowerBody(newItem);
        break;
      case 'dress':
        setDress(newItem);
        break;
      default:
        break;
    }
  };

  const handleTitleChange = (newTitle: string) => {
    setTitle(newTitle);
  };

  const handleContentChange = (newContent: string) => {
    setContent(newContent);
  };

  const handleDeleteCloth = (part: string) => {
    switch (part) {
      case 'outerCloth':
      case 'outer':
        setOuterCloth(null);
        break;
      case 'upperBody':
      case 'upper':
        setUpperBody(null);
        break;
      case 'lowerBody':
      case 'lower':
        setLowerBody(null);
        break;
      case 'dress':
        setDress(null);
        break;
      default:
        break;
    }
  };

  // 피드 등록 버튼
  const handleCreateFeed = () => {
    if (!outerCloth && !upperBody && !lowerBody && !dress) return;

    const coordiCreateRequestDTO = {
      outerCloth: {
        style: outerCloth?.style,
        category: outerCloth?.category,
        color: outerCloth?.color,
      },
      upperBody: {
        style: upperBody?.style,
        category: upperBody?.category,
        color: upperBody?.color,
      },
      lowerBody: {
        style: lowerBody?.style,
        category: lowerBody?.category,
        color: lowerBody?.color,
      },
      dress: {
        style: dress?.style,
        category: dress?.category,
        color: dress?.color,
      },
    };

    const feedCreateRequestDTO = {
      feedTitle: title,
      feedContent: content,
      outerCloth: outerCloth?.imageUrl,
      upperBody: upperBody?.imageUrl,
      lowerBody: lowerBody?.imageUrl,
      dress: dress?.imageUrl,
    };

    const request = {
      feedCreateRequest: feedCreateRequestDTO,
      coordiCreateRequest: coordiCreateRequestDTO,
    };

    api
      .createFeedCoordi(request)
      .then(() => {
        navigate('/feed');
      })
      .catch((error: any) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  // 카카오톡 공유 버튼
  const handleShareToKakao = () => {};

  // 검색 필터 토글 버튼
  const toggleSearch = () => {
    setIsSearchVisible(!isSearchVisible);
  };

  // 추천 리스트 토글 버튼
  const toggleRecommendList = () => {
    setIsRecommendListVisible(!isRecommendListVisible);
  };

  const toggleStyle = (style: string) => {
    if (selectedStyles.includes(style)) {
      setSelectedStyles(selectedStyles.filter((item) => item !== style));
    } else {
      setSelectedStyles([...selectedStyles, style]);
    }
  };

  const toggleCategory = (category: string) => {
    if (selectedCategories.includes(category)) {
      setSelectedCategories(selectedCategories.filter((item) => item !== category));
    } else {
      setSelectedCategories([...selectedCategories, category]);
    }
  };

  const toggleColor = (color: string) => {
    if (selectedColors.includes(color)) {
      setSelectedColors(selectedColors.filter((item) => item !== color));
    } else {
      setSelectedColors([...selectedColors, color]);
    }
  };

  const getRecommends = () => {
    const filter: SearchFilter = {
      style: selectedStyles,
      category: selectedCategories,
      color: selectedColors,
    };

    console.log(filter);

    api
      .getRecommends(filter)
      .then((response) => {
        const data = response.data;
        setOuterClothes(data?.outerCloth);
        setUpperBodys(data?.upperBody);
        setLowerBodys(data?.lowerBody);
        setDresses(data?.dress);
        console.log(data);
      })
      .then(() => {
        setIsRecommendListVisible(true);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  // 검색 버튼
  const handleSearchItems = () => {
    getRecommends();
  };

  // 옷장의 아이템을 store 저장 완료, 값 반영
  const handleClothStore = () => {
    if (clothStore.cloth != null) {
      switch (clothStore.cloth.part) {
        case 'outerCloth':
        case 'outer':
          setOuterCloth(clothStore.cloth);
          break;
        case 'upperBody':
        case 'upper':
          setUpperBody(clothStore.cloth);
          break;
        case 'lowerBody':
        case 'lower':
          setLowerBody(clothStore.cloth);
          break;
        case 'dress':
          setDress(clothStore.cloth);
          break;
        default:
          break;
      }

      clothStore.deleteCloth();
    }
  };

  // Coordi 불러 오기
  const getCoordi = (feedId: number) => {
    feedApi
      .readCoordi(Number(feedId))
      .then((response) => {
        const data = response.data;

        if (data.outerCloth.image != null) setOuterCloth(data?.outerCloth);
        if (data.upperBody.image != null) setUpperBody(data?.upperBody);
        if (data.lowerBody.image != null) setLowerBody(data?.lowerBody);
        if (data.dress.image != null) setDress(data?.dress);

        console.log(data);
      })
      .then(() => {
        setIsRecommendListVisible(true);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  useEffect(() => {
    if (feedId != null) getCoordi(Number(feedId));
    getRecommends();
  }, [feedId]);

  return (
    <>
      <Navbar></Navbar>
      <div className="grid px-20 mx-auto my-16 justify-items-center">
        <div className="justify-around">
          <div className="p-8 m-2 rounded-lg">
            <div className="flex justify-center">
              <div className="mx-8 my-2">
                <div className="flex justify-center">
                  <div>아우터 </div>
                  <button className="text-gray-400" onClick={() => handleDeleteCloth('outerCloth')}>
                    (삭제)
                  </button>
                </div>
                {!outerCloth ? <CoordiSelectedItem item={null} id="outer"></CoordiSelectedItem> : <CoordiSelectedItem item={outerCloth} id="outer"></CoordiSelectedItem>}
              </div>
              <div className="mx-8 my-2">
                <div className="flex justify-center">
                  <div>상의 </div>
                  <button className="text-gray-400" onClick={() => handleDeleteCloth('upperBody')}>
                    (삭제)
                  </button>
                </div>
                {!upperBody ? <CoordiSelectedItem item={null} id="upper"></CoordiSelectedItem> : <CoordiSelectedItem item={upperBody} id="upper"></CoordiSelectedItem>}
              </div>
              <div className="mx-8 my-2">
                <div className="flex justify-center">
                  <div>하의 </div>
                  <button className="text-gray-400" onClick={() => handleDeleteCloth('lowerBody')}>
                    (삭제)
                  </button>
                </div>
                {!lowerBody ? <CoordiSelectedItem item={null} id="lower"></CoordiSelectedItem> : <CoordiSelectedItem item={lowerBody} id="lower"></CoordiSelectedItem>}
              </div>
              <div className="mx-8 my-2">
                <div className="flex justify-center">
                  <div>드레스 </div>
                  <button className="text-gray-400" onClick={() => handleDeleteCloth('dress')}>
                    (삭제)
                  </button>
                </div>
                {!dress ? <CoordiSelectedItem item={null} id="dress"></CoordiSelectedItem> : <CoordiSelectedItem item={dress} id="dress"></CoordiSelectedItem>}
              </div>
            </div>
            <div className="">
              <div className="">
                {isSearchVisible && (
                  <div className="py-2 my-2">
                    <div className="my-8">
                      <h2 className="pl-2 ml-2">스타일</h2>
                      <div className="flex flex-wrap pl-2 ml-2">
                        {styles.map((style, index) => (
                          <button
                            key={index}
                            className={`rounded-full border-2 px-4 py-2 my-2 mr-2 shadow-md border-md cursor-pointer ${selectedStyles.includes(style) ? 'bg-gray-900 text-white' : 'bg-white text-black'}`}
                            onClick={() => toggleStyle(style)}
                          >
                            {style}
                          </button>
                        ))}
                      </div>
                    </div>
                    <div className="my-8">
                      <h2 className="pl-2 ml-2">카테고리</h2>
                      <div className="flex flex-wrap pl-2 ml-2">
                        {categories.map((category, index) => (
                          <button
                            key={index}
                            className={`rounded-full border-2 px-4 py-2 my-2 mr-2 shadow-md border-md cursor-pointer ${selectedCategories.includes(category) ? 'bg-gray-900 text-white' : 'bg-white text-black'}`}
                            onClick={() => toggleCategory(category)}
                          >
                            {category}
                          </button>
                        ))}
                      </div>
                    </div>
                    <div className="my-8">
                      <h2 className="pl-2 ml-2">색상</h2>
                      <div className="flex flex-wrap pl-2 ml-2">
                        {colors.map((color, index) => (
                          <button
                            key={index}
                            className={`rounded-full border-2 px-4 py-2 my-2 mr-2 shadow-md border-md cursor-pointer ${selectedColors.includes(color) ? 'bg-gray-900 text-white' : 'bg-white text-black'}`}
                            value={color}
                            onClick={() => toggleColor(color)}
                          >
                            {color}
                          </button>
                        ))}
                      </div>
                    </div>
                    <button value="검색" onClick={() => handleSearchItems()} />
                  </div>
                )}
              </div>
            </div>
            <div className="flex justify-between p-4 m-2">
              <div className="p-2">
                <CustomButton value="내 옷장" onClick={clickModal} />
              </div>
              <div className="flex">
                <div className="p-2">
                  {isRecommendListVisible ? <WhiteButton onClick={toggleRecommendList} value="추천 리스트 닫기" /> : <WhiteButton onClick={toggleRecommendList} value="추천 리스트 열기" />}
                </div>
                <div className="p-2">{isSearchVisible ? <WhiteButton onClick={toggleSearch} value="검색 필터 닫기" /> : <WhiteButton onClick={toggleSearch} value="검색 필터 열기" />}</div>
                <div className="p-2">
                  <CustomButton value="검색" onClick={() => handleSearchItems()} />
                </div>
              </div>
            </div>
            <div className="">
              <div className="w-auto">
                {isRecommendListVisible && (
                  <div>
                    {outerClothes.length == 0 ||
                      outerClothes.length == 0 ||
                      outerClothes.length == 0 ||
                      (outerClothes.length == 0 && (
                        <div className="mx-4 my-20">
                          <div className="text-center">검색된 추천 리스트가 없습니다!</div>
                        </div>
                      ))}
                    <div className="flex justify-center">
                      <div className="mx-4 my-2">
                        <div className="text-center">아우터</div>
                        {dresses.length == 0 ? (
                          <div className="mx-4 my-20">
                            <div className="w-64 my-20 text-center">검색된 추천 리스트가 없습니다!</div>
                          </div>
                        ) : (
                          <div>
                            {outerClothes.map((item, index) => (
                              <CoordiItem key={index} item={item} onClickItem={() => handleClickItem(item)}></CoordiItem>
                            ))}
                          </div>
                        )}
                      </div>
                      <div className="mx-4 my-2">
                        <div className="text-center">상의</div>
                        {dresses.length == 0 ? (
                          <div className="mx-4 my-20">
                            <div className="w-64 my-20 text-center">검색된 추천 리스트가 없습니다!</div>
                          </div>
                        ) : (
                          <div>
                            {upperBodys.map((item, index) => (
                              <CoordiItem key={index} item={item} onClickItem={() => handleClickItem(item)}></CoordiItem>
                            ))}
                          </div>
                        )}
                      </div>
                      <div className="mx-4 my-2">
                        <div className="text-center">하의</div>
                        {dresses.length == 0 ? (
                          <div className="mx-4 my-20">
                            <div className="w-64 my-20 text-center">검색된 추천 리스트가 없습니다!</div>
                          </div>
                        ) : (
                          <div>
                            {lowerBodys.map((item, index) => (
                              <CoordiItem key={index} item={item} onClickItem={() => handleClickItem(item)}></CoordiItem>
                            ))}
                          </div>
                        )}
                      </div>

                      <div className="mx-4 my-2">
                        <div className="text-center">드레스</div>
                        {dresses.length == 0 ? (
                          <div className="mx-4 my-20">
                            <div className="w-64 my-20 text-center">검색된 추천 리스트가 없습니다!</div>
                          </div>
                        ) : (
                          <div>
                            {dresses.map((item, index) => (
                              <CoordiItem key={index} item={item} onClickItem={() => handleClickItem(item)}></CoordiItem>
                            ))}
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                )}
              </div>
            </div>
            <div className="p-2 m-6 border-2 rounded-md">
              <div className="p-2 m-2">
                <Input className="p-2 m-2 border-2 rounded-md" type="text" id="title" value={title} onChange={(event) => handleTitleChange(event.target.value)} label="피드 제목" />
              </div>
              <div className="p-2 m-2">
                <CustomTextArea
                  className="p-2 m-2 border-2 rounded-md"
                  id="content"
                  value={content}
                  onChange={(event) => handleContentChange(event.target.value)}
                  rows={4}
                  cols={50}
                  label="피드 내용"
                />
              </div>
              <div className="flex justify-end p-2 m-2">
                <CustomButton className="p-2 mr-2" value="피드 등록" onClick={() => handleCreateFeed()} />
                <CustomButton className="p-2 ml-2" value="카카오톡 공유" onClick={() => handleShareToKakao} />
              </div>
            </div>
          </div>
        </div>
      </div>
      <Modal isOpen={isOpenModal} onClose={closeModal} classN="w-2/3 h-4/5">
        <div>내 옷장</div>
        <MyClosetReadModal onClose={closeModal} handleClothStore={handleClothStore} />
      </Modal>
    </>
  );
};

export default CoordiFromFeed;
