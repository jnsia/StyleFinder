import { ButtonHTMLAttributes } from 'react';
import styled from 'styled-components';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {}

const ButtonElement = styled.button`
  padding: 0.7rem 1.2rem;
  border-radius: 0.5rem;
  background-color: #ffffff;
  color: #333333;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: lightgrey;
  }
`;

const WhiteButton = (props: ButtonProps) => {
  return (
    <ButtonElement className="border-2 shadow-2 border-md" {...props}>
      {props.value}
    </ButtonElement>
  );
};

export default WhiteButton;
