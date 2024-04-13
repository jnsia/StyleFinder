import Navbar from '../../widgets/nav/navbar';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import main4 from '../../assets/images/main4.jpg';
import main5 from '../../assets/images/main5.jpg';
import main6 from '../../assets/images/main6.jpg';
import main7 from '../../assets/images/main7.jpg';
import main8 from '../../assets/images/main8.jpg';
import './main.css';

const Main = () => {
  const images = [main4, main8, main5];
  const images1 = [main5,main6,main7];

  const commonSettings = {
    dots: false,
    arrows: false,
    infinite: true,
    speed: 1000,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 2000,
    pauseOnHover: true,
    vertical: true,
    fade: true, // 이미지가 서서히 나타나게 함
    cssEase: 'linear', // 서서히 나타나는 애니메이션 효과 설정
  };

  return (
    <>
      <Navbar />
      <div className="">
        <div className="items-center justify-between min-h-screen gap-8 hero bg-base-200 lg:flex lg:flex-row lg:gap-0">
          <div className="text-center lg:w-1/2 lg:text-left">
            <h1 className="text-5xl font-bold text-center">오늘의 코디는?</h1>
            <p className="flex justify-center py-6">추천 받으세요</p>
          </div>
          <div className="lg:w-1/3">
            <Slider {...commonSettings}>
              {images.map((image, index) => (
                <div key={index} className="image-wrapper">
                  <img src={image} alt={`slide-${index}`} className="img" />
                </div>
              ))}
            </Slider>
          </div>
          <div className="lg:w-1/3">
  <Slider {...commonSettings}>
    {images1.map((image, index) => (
      <div key={index} className="image-wrapper">
        <img src={image} alt={`slide-${index}`} className="img" />
      </div>
    ))}
  </Slider>
</div>

        </div>
      </div>
    </>
  );
};

export default Main;
