/* eslint-disable @typescript-eslint/no-explicit-any */
export const axiosError = (error: any) => {
  const errorCode = error.response?.status;
  const errorMessage = error.response?.data.message;

  if (errorCode == 500) {
    alert("정상 작동하지 않았습니다.");
  } else {
    alert(errorMessage);
  }

  return errorCode;
};
