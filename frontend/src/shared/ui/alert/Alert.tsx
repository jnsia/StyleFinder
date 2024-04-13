import styled from 'styled-components';
import Button from '../button/custom-button';

interface AlertProps {
  message: string;
  onClickOK(): void;
}

const AlertDiv = styled.div`
  width: 300px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
`;

const TitleElement = styled.h2`
  margin-bottom: 10px;
`;

const MessageElement = styled.div`
  margin-bottom: 20px;
`;

const ButtonDiv = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const ButtonElement = styled(Button)`
  margin-left: 10px;
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
  background-color: #3b82f6;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #2563eb;
  }
`;

const Alert = (props: AlertProps) => {
  return (
    <AlertDiv>
      <TitleElement>Alert</TitleElement>
      <MessageElement>{props.message}</MessageElement>
      <ButtonDiv>
        <ButtonElement onClick={props.onClickOK} value="ok" />
      </ButtonDiv>
    </AlertDiv>
  );
};

export default Alert;
