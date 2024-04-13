import { ButtonHTMLAttributes } from 'react';
import styled from 'styled-components';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {}

const ButtonElement = styled.button`
  padding: 0.7rem 1.2rem;
  border-radius: 0.5rem;
  background-color: #161a30;
  color: #ffffff;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #2563eb;
  }
`;

const CustomButton = (props: ButtonProps) => {
  return (
    <ButtonElement className="border-2 shadow-md border-md" {...props}>
      {props.value}
    </ButtonElement>
  );
};

export default CustomButton;
