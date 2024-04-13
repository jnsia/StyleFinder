import Navbar from '../../widgets/nav/navbar';

import { Outlet } from 'react-router-dom';
import NestedNavbar from '../../widgets/nav/nested-navbar';

const Analysis = () => {
  return (
    <>
      <Navbar></Navbar>
      <div className="container mx-auto px-36">
        <NestedNavbar />
        <Outlet />
      </div>
    </>
  );
};

export default Analysis;
