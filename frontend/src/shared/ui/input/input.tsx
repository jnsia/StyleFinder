/* eslint-disable @typescript-eslint/no-explicit-any */
import { InputHTMLAttributes } from 'react';
import styled from 'styled-components';

interface InputBarProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const InputDiv = styled.div`
  margin-bottom: 1rem;
`;

const LabelElement = styled.label`
  display: block;
  margin-bottom: 0.5rem;
`;

const InputElement = styled.input`
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 0.375rem;
`;

const Input = (props: InputBarProps) => {
  return (
    <InputDiv>
      <LabelElement>{props.label}</LabelElement>
      <InputElement type="text" value={props.value} onChange={props.onChange} />
    </InputDiv>
  );
};

export default Input;
