import { TextareaHTMLAttributes } from 'react';
import TextareaAutosize from 'react-textarea-autosize';
import styled from 'styled-components';

interface TextAreaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  label: string;
  onChange: (event: React.ChangeEvent<HTMLTextAreaElement>) => void;
}

const TextAreaDiv = styled.div`
  margin-bottom: 1rem;
`;

const LabelElement = styled.label`
  display: block;
  margin-bottom: 0.5rem;
`;

const TextareaElement = styled(TextareaAutosize)`
  width: 100%;
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 0.375rem;
`;

const CustomTextArea = (props: TextAreaProps) => {
  return (
    <TextAreaDiv>
      <LabelElement>{props.label}</LabelElement>
      <TextareaElement cacheMeasurements placeholder="" value={props.value} onChange={props.onChange} />
    </TextAreaDiv>
  );
};

export default CustomTextArea;
