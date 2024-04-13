import styled from 'styled-components';
import CustomButton from '../button/custom-button';

interface ModalProps {
  classN: string;
  isOpen: boolean;
  onClose: () => void;
  children?: React.ReactNode;
}

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const ModalCloseButton = styled(CustomButton)`
  position: absolute;
  top: 10px;
  right: 10px;
`;

const Modal = (props: ModalProps) => {
  if (!props.isOpen) return null;

  return (
    <ModalOverlay>
      <ModalContent className={props.classN}>
        <ModalCloseButton onClick={props.onClose} value="close" />
        <div className="max-w-196 max-h-196">{props.children}</div>
      </ModalContent>
    </ModalOverlay>
  );
};

export default Modal;
