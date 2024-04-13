/* eslint-disable @typescript-eslint/no-explicit-any */
import Navbar from '../../widgets/nav/navbar';
import { useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // Link import
import './feed.css';
import api from '../../entities/feed/feed-apis';
import { axiosError } from '../../shared/utils/axiosError';
import useLoginStore from '../../shared/store/use-login-store';
import noimage from '../../assets/images/noimage.png';
import CustomButton from '../../shared/ui/button/custom-button';
import WhiteButton from '../../shared/ui/button/white-button';

const Feed = () => {
  const navigate = useNavigate();
  const loginStore = useLoginStore();

  const unselected =
    'relative h-10 max-h-[40px] w-10 max-w-[40px] select-none rounded-full text-center align-middle font-sans text-xs font-medium uppercase text-gray-900 transition-all hover:bg-gray-900/10 active:bg-gray-900/20 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none';
  const selected =
    'relative h-10 max-h-[40px] w-10 max-w-[40px] select-none rounded-full bg-gray-900 text-center align-middle font-sans text-xs font-medium uppercase text-white shadow-md shadow-gray-900/10 transition-all hover:shadow-lg hover:shadow-gray-900/20 focus:opacity-[0.85] focus:shadow-none active:opacity-[0.85] active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none';

  const [isOverlayVisible, setIsOverlayVisible] = useState('0');
  const [ModalOpen, setModalOpen] = useState(0);
  const [feeds, setFeeds] = useState([]);
  const [query, setQuery] = useState('');
  const [page, setPage] = useState(1);
  const [pageList, setPageList] = useState([1]);
  const [feedListType, setFeedListType] = useState('all');

  const modalBackground = useRef(null);

  const handleMouseEnter = (feedId: string) => {
    setIsOverlayVisible(feedId);
  };

  const handleMouseLeave = () => {
    setIsOverlayVisible('0');
  };

  const getFeeds = () => {
    api
      .readFeedList(page - 1)
      .then((response) => {
        const data = response.data.data;

        if (feedListType != 'all') {
          setPage(1);
        }

        setFeeds(data);
        setFeedListType('all');

        console.log(data);

        const totalPage = response.data.totalPage;
        return totalPage;
      })
      .then((endPage) => {
        updatePageList(endPage);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  const getPopularFeed = () => {
    api
      .readPopularFeedList(page - 1)
      .then((response) => {
        const data = response.data.data;

        if (feedListType != 'popular') {
          setPage(1);
        }

        setFeeds(data);
        setFeedListType('popular');

        const totalPage = response.data.totalPage;
        return totalPage;
      })
      .then((endPage) => {
        updatePageList(endPage);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  const getMyFeed = () => {
    api
      .readMyFeed(page - 1)
      .then((response) => {
        const data = response.data.data;

        if (feedListType != 'my') {
          setPage(1);
        }

        setFeeds(data);
        setFeedListType('my');

        const totalPage = response.data.totalPage;
        return totalPage;
      })
      .then((endPage) => {
        updatePageList(endPage);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  const searchFeed = () => {
    if (query === '') {
      return;
    }

    api
      .searchByTitle(query, page - 1)
      .then((response) => {
        const data = response.data.data;

        if (feedListType != 'search') {
          setPage(1);
        }

        setFeeds(data);
        setFeedListType('search');

        const totalPage = response.data.totalPage;
        return totalPage;
      })
      .then((endPage) => {
        updatePageList(endPage);
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  const updatePageList = (endPage: number) => {
    const numberArray = [];
    let count = 0;

    for (let i = endPage; ; i--) {
      if (i == 0) break;
      if (count == 5) break;
      numberArray.unshift(i);
      count++;
    }

    setPageList(numberArray);
  };

  useEffect(() => {
    if (feedListType === 'all') {
      getFeeds();
    } else if (feedListType === 'my') {
      getMyFeed();
    } else if (feedListType === 'popular') {
      getPopularFeed();
    } else if (feedListType === 'search') {
      searchFeed();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  return (
    <>
      <Navbar></Navbar>
      <div className="mx-auto px-36 ">
        <div className="flex justify-around pt-20">
          <WhiteButton className="p-2 m-2 border-2" onClick={getPopularFeed} value="인기순 조회" />
          <div className="w-100">
            <input className="p-2 m-2 border-2" value={query} onChange={(event) => setQuery(event.target.value)} required></input>
            <CustomButton
              onClick={() => {
                searchFeed();
                setPage(1);
              }}
              value="검색"
            />
          </div>
          <WhiteButton className="p-2 m-2 border-2" onClick={getMyFeed} value="내 피드 조회" />
        </div>
        <div className="grid grid-flow-row-dense grid-cols-4">
          {feeds.map((feed: any) => (
            <div key={feed?.feedId}>
              <div className="p-2">
                <div className="relative card bg-base-100" onMouseEnter={() => handleMouseEnter(feed.feedId)} onMouseLeave={handleMouseLeave}>
                  <div className="card-body">
                    <figure>
                      <div className="flex flex-col">
                        <div className="flex flex-row">
                          {feed?.outerCloth ? (
                            <img src={`data:image/png;base64,${feed?.outerCloth}`} alt="Outer Cloth" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          ) : (
                            <img src={noimage} alt="Default Outer Cloth" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          )}
                          {feed?.dress ? (
                            <img src={`data:image/png;base64,${feed?.dress}`} alt="Dress" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          ) : (
                            <img src={noimage} alt="Default Dress" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          )}
                        </div>
                        <div className="flex flex-row">
                          {feed?.upperBody ? (
                            <img src={`data:image/png;base64,${feed?.upperBody}`} alt="Upper Body" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          ) : (
                            <img src={noimage} alt="Default Upper Body" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          )}
                          {feed?.lowerBody ? (
                            <img src={`data:image/png;base64,${feed?.lowerBody}`} alt="Lower Body" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          ) : (
                            <img src={noimage} alt="Default Lower Body" className="w-32 h-32 border-2 rounded-md max-h-32" />
                          )}
                        </div>
                      </div>
                    </figure>
                    {ModalOpen === feed.feedId && (
                      <div
                        className={'modal-container'}
                        ref={modalBackground}
                        onClick={(e) => {
                          if (e.target === modalBackground.current) {
                            setModalOpen(0);
                          }
                        }}
                      >
                        <div className="flex items-center w-1/2 rounded-md modal-content h-2/5 justify-evenly ">
                          <div className="pl-5 pr-5 basis-1/3">
                            <img src={`data:image/png;base64,${feed.user.profileImage}`} alt="feedImage" className="ml-5 rounded-lg w-36 h-36" />
                            <p>{feed.user.instagram}</p>
                            <p>{feed.user.youtube}</p>
                          </div>
                          <div className="flex flex-col basis-2/3">
                            <p className="pb-3 text-xl">{feed.user.nickname}</p>
                            <div className="introduction-box">
                              <p>{feed.user.introduce}</p>
                            </div>
                            <div className="flex pt-2 likebox">
                              {feed.user.likeCategories.map((category: string) => (
                                <p className="pt-2 pr-2">{category}</p>
                              ))}
                            </div>
                            <div className="flex justify-end pt-2 mr-24">
                              <CustomButton className="modal-close-btn closebutton" onClick={() => setModalOpen(0)} value="닫기" />
                            </div>
                          </div>
                        </div>
                      </div>
                    )}
                  </div>
                  <Link to={`/feeddetail/${feed.feedId}`}>
                    {isOverlayVisible == feed.feedId && (
                      <div className="absolute inset-0 w-64 h-64 bg-black border-2 rounded-md opacity-50 max-h-64">
                        <div className="absolute inset-0 flex flex-col items-center justify-center text-white">
                          {feed.coordiContainer?.outerCloth?.category && <p>아우터: {feed.coordiContainer?.outerCloth?.category}</p>}
                          {feed.coordiContainer?.upperBody?.category && <p>상의: {feed.coordiContainer?.upperBody?.category}</p>}
                          {feed.coordiContainer?.lowerBody?.category && <p>하의: {feed.coordiContainer?.lowerBody?.category}</p>}
                          {feed.coordiContainer?.dress?.category && <p>드레스: {feed.coordiContainer?.dress?.category}</p>}
                        </div>
                      </div>
                    )}
                  </Link>
                </div>
                <div className="flex justify-between">
                  <button className={'modal-open-btn'} onClick={() => setModalOpen(feed.feedId)}>
                    <div className="grid w-full grid-cols-12 gap-4 pt-2">
                      <div className="flex items-center col-span-4">{feed?.user.profileImage && <img src={`data:image/png;base64,${feed.user.profileImage}`} className="w-12 h-12 rounded-lg" />}</div>

                      <div className="col-span-5">
                        <div>
                          <div>{feed.user.nickname}</div>
                          <h2>{feed?.feedTitle}</h2>
                        </div>
                      </div>

                      <div className="flex items-center justify-end col-span-2">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                          <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
                        </svg>
                        <div>{feed?.feedLikes}</div>
                      </div>
                    </div>
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
        <div className="flex items-center justify-center gap-4 my-10">
          <div className="flex items-center gap-2">
            {pageList.map((pageNumber) => (
              <button key={pageNumber} className={page === pageNumber ? selected : unselected} type="button" value={pageNumber} onClick={() => setPage(pageNumber)}>
                <span className="absolute transform -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2">{pageNumber}</span>
              </button>
            ))}
          </div>
        </div>
      </div>
    </>
  );
};

export default Feed;
