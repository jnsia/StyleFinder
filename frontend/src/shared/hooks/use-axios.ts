import { useEffect, useState } from 'react';
import { AxiosError, AxiosResponse } from 'axios';

type RequestFn<T> = (params: T) => Promise<AxiosResponse>;

const useAxios = <T>(requestFn: RequestFn<T>, params: T) => {
  const [response, setResponse] = useState<AxiosResponse>();
  const [error, setError] = useState<AxiosError>();
  const [isLoading, setIsLoading] = useState(false);

  const fetchData = async () => {
    setResponse(undefined);
    setError(undefined);
    setIsLoading(true);

    try {
      const response = await requestFn(params);
      setResponse(response);
    } catch (error) {
      setError(error as AxiosError);
    }
    setIsLoading(false);
  };

  const sendRequest = async () => {
    fetchData();
  };

  useEffect(() => {
    fetchData();
  },[]);

  return { response, error, isLoading, sendRequest };
};

export default useAxios;