import My from '../my/my';
import MyCloset from './my-closet';

const Closet = () => {
  return (
    <div className="mx-auto px-36">
      <div className="justify-around pt-20">
        <div>
          <My></My>
        </div>
        <div className="pt-2 mt-2">
          <MyCloset></MyCloset>
        </div>
      </div>
    </div>
  );
};

export default Closet;
