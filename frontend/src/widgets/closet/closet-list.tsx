// import Button from '../../shared/ui/button/button';
// import Image from '../../assets/images/aimodel.png';

// import { useState } from 'react';

// const ClosetList = () => {
//   const props = ['1', '2', '3'];

//   const [id, setId] = useState<number>(0);
//   const [imageSrc, setImageSrc] = useState<string>(Image);

//   const handleClickCloset = (): void => {
//     id;
//   };

//   const handleClickItem = (newId: number, newImageSrc: string): void => {
//     setId(newId);
//     setImageSrc(newImageSrc);
//   };

//   return (
//     <div>
//       <div className="flex">
//         <img className="p-2 m-2 bg-gray-200 rounded-lg" src={imageSrc} alt="" id="selectedImage" />
//         <Button onClick={handleClickCloset} value="move to closet" />

//         <div className="flex">
//           {props.map((item, index) => (
//             <div>
//               <img className="p-2 m-2 bg-gray-200 rounded-lg" src={imageSrc} alt="" id="viewImages" />
//               <Button
//                 onClick={() => {
//                   handleClickItem(index, item);
//                 }}
//                 value="Links"
//               />
//             </div>
//           ))}
//         </div>

//         <div>🔄</div>
//       </div>
//     </div>
//   );
// };

// export default ClosetList;
