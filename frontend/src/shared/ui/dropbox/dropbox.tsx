import { useState } from 'react';
import styled from 'styled-components';

interface DropboxProps {
  options: string[];
  onSelected: (selectedItem: string) => void;
}

const DropdownDiv = styled.div`
  position: relative;
  display: inline-block;
`;

const SelectedItem = styled.button`
  padding: 0.7rem 1.2rem;
  border-radius: 0.5rem;
  border: 3px solid lightgrey;
  color: #000000;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: lightgrey;
  }
`;

const DropdownList = styled.div`
  position: absolute;
  border-radius: 0.5rem;
  top: 100%;
  left: 0;
  background-color: #fff;
  border: 1px solid #ccc;
  border-top: none;
  z-index: 1;
`;

const DropdownItem = styled.div`
  white-space: nowrap;
  border-radius: 0.5rem;
  padding: 8px 12px;
  cursor: pointer;
  &:hover {
    background-color: lightgrey;
  }
`;

const Dropbox = (props: DropboxProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<string | null>(null);

  const handleItemClick = (item: string) => {
    setSelectedItem(item);
    setIsOpen(false);
    props.onSelected(item);
  };

  return (
    <DropdownDiv>
      <SelectedItem className="w-64 h-auto mx-4 border-2 rounded-md my-2s" onClick={() => setIsOpen(!isOpen)}>
        {selectedItem || '선택'}
      </SelectedItem>
      {isOpen && (
        <DropdownList className="w-64 h-auto mx-4 border-2 rounded-md">
          {props.options.map((item, index) => (
            <DropdownItem key={index} onClick={() => handleItemClick(item)}>
              {item}
            </DropdownItem>
          ))}
        </DropdownList>
      )}
    </DropdownDiv>
  );
};

export default Dropbox;
