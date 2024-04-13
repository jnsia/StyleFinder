import { useState } from 'react';
import { Link } from 'react-router-dom';
import './nav.css';

const NestedNavbar = () => {
  const [selectedButton, setSelectedButton] = useState<string>('closet');

  const handleButtonClick = (buttonName: string) => {
    setSelectedButton(buttonName);
  };

  return (
    <div className="navbar flex justify-center">
      <ul className="menu">
        <div className="navbar-center flex flex-row">
          <div>
            <li>
              <Link to="/analysis/closet" replace={true} className={`check ${selectedButton === 'closet' ? 'selected' : ''}`} onClick={() => handleButtonClick('closet')}>
                내 옷장
              </Link>
            </li>
          </div>
          <div>
            <li>
              <Link to="/analysis/recommendation" replace={true} className={`check ${selectedButton === 'recommendation' ? 'selected' : ''}`} onClick={() => handleButtonClick('recommendation')}>
                코디 추천
              </Link>
            </li>
          </div>
        </div>
      </ul>
    </div>
  );
};

export default NestedNavbar;
