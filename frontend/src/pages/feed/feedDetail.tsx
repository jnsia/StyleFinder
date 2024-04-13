/* eslint-disable @typescript-eslint/no-unused-vars */
/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import Navbar from '../../widgets/nav/navbar';
import { useNavigate, useParams } from 'react-router';
import api from '../../entities/feed/feed-apis';
import { axiosError } from '../../shared/utils/axiosError';
import { FeedInfo } from '../../entities/feed/feed-types';
import noimage from '../../assets/images/noimage.png';
import './feed.css';
import commentApi from '../../entities/comment/comment-apis';
import useLoginStore from '../../shared/store/use-login-store';

const FeedDetail: React.FC = () => {
  const navigate = useNavigate();
  const loginStore = useLoginStore();

  const { feedId } = useParams<{ feedId: string }>();
  const [feedInfo, setFeedInfo] = useState<FeedInfo>();
  const [commentText, setCommentText] = useState('');
  const [feedLikes, setFeedLikes] = useState<number>(feedInfo?.feedLikes || 0);
  const [isLiked, setIsLiked] = useState<boolean>(false);
  const handleChangeComment = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentText(event.target.value);
  };

  const handleLikeClick = () => {
    if (!isLiked) {
      setFeedLikes(feedLikes + 1);
    } else {
      setFeedLikes(feedLikes - 1);
    }

    api
      .likeFeed(Number(feedId))
      .then(() => {
        getFeedDetail();
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  const handleSubmitComment = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    commentApi
      .createComment(Number(feedId), commentText)
      .then(() => {
        getFeedDetail();
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });

    setCommentText('');
  };

  const handleClickMoveToCoordi = (feedId: number) => {
    navigate(`/coordi/1/${feedId}`);
  };

  // const handleIconClick = () => {
  //   setIsChecked(!isChecked);
  //   setLikesCount(isChecked ? likesCount - 1 : likesCount + 1);
  // };

  const getFeedDetail = () => {
    api
      .readFeed(Number(feedId))
      .then((response) => {
        const data = response.data.data;
        setFeedInfo(data);
        setIsLiked(data.user.isLiked);
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
    getFeedDetail();
  }, []);

  return (
    <>
      <Navbar />
      <div className="h-full pt-5 mx-auto px-36">
        <div className="flex flex-col min-w-min hero h-full bg-base-200 bg-[#161A30] text-color p-8 rounded-md">
          <div className="flex flex-row pb-5">
            {feedInfo?.user.profileImage && <img src={`data:image/png;base64,${feedInfo?.user.profileImage}`} alt="profileImage" className="w-16 h-16 rounded-full" />}
            <div className="flex items-center pl-5 author-name">작성자 닉네임: {feedInfo?.user.nickname}</div>
          </div>
          <div className="flex flex-row">
            <div className="flex flex-col">
              <div className="flex flex-row">
                <div className="flex flex-row">
                  <div className="flex flex-col p-3">
                    <div className="flex justify-center">아우터</div>
                    <div>
                      {feedInfo?.outerCloth ? (
                        <img src={`data:image/png;base64,${feedInfo?.outerCloth}`} alt="Outer Cloth" className="w-40 h-40 rounded-md" />
                      ) : (
                        <img src={noimage} alt="Default Outer Cloth" className="w-40 h-40 rounded-md" />
                      )}
                    </div>
                  </div>
                  <div className="p-3">
                    <div className="flex justify-center">드레스</div>
                    <div>
                      {feedInfo?.dress ? (
                        <img src={`data:image/png;base64,${feedInfo?.dress}`} alt="Dress" className="w-40 h-40 rounded-md" />
                      ) : (
                        <img src={noimage} alt="Default Dress" className="w-40 h-40 rounded-md" />
                      )}
                    </div>
                  </div>
                </div>
              </div>
              <div className="flex flex-row">
                <div className="flex flex-row">
                  <div className="flex flex-row">
                    <div className="p-3">
                      <div className="flex justify-center">상의</div>
                      <div>
                        {feedInfo?.upperBody ? (
                          <img src={`data:image/png;base64,${feedInfo?.upperBody}`} alt="Upper Body" className="w-40 h-40 rounded-md" />
                        ) : (
                          <img src={noimage} alt="Default Upper Body" className="w-40 h-40 rounded-md" />
                        )}
                      </div>
                    </div>
                    <div className="p-3">
                      <div className="flex justify-center">하의</div>
                      <div>
                        {feedInfo?.lowerBody ? (
                          <img src={`data:image/png;base64,${feedInfo?.lowerBody}`} alt="Lower Body" className="w-40 h-40 rounded-md" />
                        ) : (
                          <img src={noimage} alt="Default Lower Body" className="w-40 h-40 rounded-md" />
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="pl-8">
              <div className="flex pb-5 author-name">피드 제목: {feedInfo?.feedTitle}</div>
              <div className="pb-5">피드 내용: {feedInfo?.feedContent}</div>
              <hr className="hr" />
              <div className="flex flex-col justify-between">
                <div className="flex justify-center pt-5 author-name">Comments</div>
                <div className="flex flex-col justify-between max-h-[270px] overflow-y-auto">
                  {feedInfo?.comments.map((comment) => (
                    <div key={comment.nickname + comment.content} className="flex items-center justify-center">
                      <div className="flex items-start flex-grow max-w-screen-xl mt-5 hero bg-base-200">
                        <div className="avatar">
                          <img src={`data:image/png;base64,${comment.profileImage}`} alt="commentProfileImage" className="w-10 h-10 rounded-full" />
                        </div>
                        <div className="flex justify-between flex-grow">
                          <div>
                            <div className="ml-3">{comment.nickname}</div>
                            <div className="ml-3">{comment.content}</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="flex items-center justify-between pt-3">
                  <div className="flex items-center">
                    <div className="flex items-center justify-between pt-3">
                      <form onSubmit={handleSubmitComment} className="flex items-center">
                        <input type="text" value={commentText} onChange={handleChangeComment} placeholder="댓글을 작성하세요" className="px-3 py-2 mr-2 border border-gray-300 rounded-md blackText" />
                        <button type="submit" className="btn btn-primary">
                          댓글 작성
                        </button>
                      </form>
                    </div>
                  </div>
                  <div className="flex flex-row">
                    <div className="flex items-center pl-3">
                      {feedInfo?.feedLikes} {/* 좋아요 수 표시 */}
                      <label className="pl-1 swap swap-flip text-9xl">
                        <input type="checkbox" onChange={() => {}} style={{ display: 'none' }} />
                        <div className={isLiked ? 'swap-on' : 'swap-off'} onClick={handleLikeClick}>
                          {isLiked ? (
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                              <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
                            </svg>
                          ) : (
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" className="w-6 h-6">
                              <path
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                              />
                            </svg>
                          )}
                        </div>
                      </label>
                    </div>

                    <div className="pl-4">
                      {feedInfo && (
                        <button className="btn btn-outline" onClick={() => handleClickMoveToCoordi(feedInfo?.id)}>
                          코디 해 보기
                        </button>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default FeedDetail;
