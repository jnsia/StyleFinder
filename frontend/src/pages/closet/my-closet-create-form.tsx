import api from '../../entities/closet/closet-apis';
import { axiosError } from '../../shared/utils/axiosError';

import { useRef, useState } from 'react';
import { useNavigate } from 'react-router';

import useLoginStore from '../../shared/store/use-login-store';

import Image from '../../assets/images/noimage.png';
import CustomButton from '../../shared/ui/button/custom-button';
import Dropbox from '../../shared/ui/dropbox/dropbox';

interface MyClosetCreateFormProps {
  onClose: () => void;
  getCloset: () => void;
}
const MyClosetCreateForm = (props: MyClosetCreateFormProps) => {
  const navigate = useNavigate();
  const loginStore = useLoginStore();

  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);

  const [imageURL, setImageURL] = useState<string>(Image);

  const optionDivRef = useRef<HTMLDivElement>(null);
  const fileDivRef = useRef<HTMLDivElement>(null);

  const options = ['아우터', '상의', '하의', '드레스'];

  // 드롭 박스 아이템 선택 시
  const handleSelectedDropbox = (item: string) => {
    setSelectedOption(item);
  };

  // 이미지 파일 선택 시
  const handleChangeFileInput = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      setSelectedFile(file);

      const reader = new FileReader();
      reader.onload = () => {
        if (typeof reader.result === 'string') {
          setImageURL(reader.result);
        }
      };
      reader.readAsDataURL(file);
    }
  };

  // 업로드 버튼
  const handleClickUpload = () => {
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
      fileInput.click();
    }
  };

  // 확인 버튼
  const handleClickSubmit = () => {
    if (selectedOption == null) {
      alert('부위를 선택해주세요!')
      if (optionDivRef.current) {
        optionDivRef.current.focus();
        shakeElement(optionDivRef.current);
      }
      return;
    }

    if (selectedFile == null) {
      if (fileDivRef.current) {
        fileDivRef.current.focus();
        shakeElement(fileDivRef.current);
      }
      return;
    }

    let option;
    if (selectedOption == '아우터') option = 'outerCloth';
    else if (selectedOption == '상의') option = 'upperBody';
    else if (selectedOption == '하의') option = 'lowerBody';
    else option = 'dress';

    api
      .uploadCloth(option, selectedFile)
      .then((response) => {
        const data = response.data.data;
        props.getCloset();
        console.log(data);
        props.onClose();
      })
      .catch((error) => {
        const errorCode = axiosError(error);

        if (errorCode == 401) {
          loginStore.setLogout();
          navigate('/login');
        }
      });
  };

  // Element 에 흔들기 이벤트 추가
  const shakeElement = (e: HTMLElement) => {
    e.style.animation = 'shake 0.5s';
    setTimeout(() => {
      e.style.animation = '';
    }, 500);
  };

  return (
    <>
      <div className="grid mx-4 my-2 justify-items-center">
        <div>옷 보관하기</div>
        <div>
          {imageURL && <img className="w-64 h-64 mx-4 my-2 border-2 rounded-md" src={imageURL} alt="Selected" />}
          <input className="hidden" id="fileInput" type="file" accept="image/*" onChange={handleChangeFileInput} />
        </div>
        <div ref={fileDivRef}>
          <CustomButton className="w-64 h-auto mx-4 my-2" value="옷 이미지 업로드" onClick={handleClickUpload} />
        </div>
        <div ref={optionDivRef}>
          <Dropbox options={options} onSelected={handleSelectedDropbox} />
        </div>
        <div>
          <CustomButton className="h-auto mx-4 my-2 w-28" value="취소" onClick={props.onClose} />
          <CustomButton className="h-auto mx-4 my-2 w-28" value="등록" onClick={handleClickSubmit} />
        </div>
      </div>
    </>
  );
};

export default MyClosetCreateForm;
