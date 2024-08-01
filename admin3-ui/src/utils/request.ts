import axios, {AxiosError, AxiosInstance, InternalAxiosRequestConfig, AxiosResponse} from 'axios';
import {ElMessage} from "element-plus";
import router from "../router";

const service: AxiosInstance = axios.create({
    timeout: 5000
});

service.interceptors.request.use(
    (config: InternalAxiosRequestConfig<any>) => {
        const headerToken = localStorage.getItem('token');
        if (headerToken) {
            // @ts-ignore
            config.headers['Authorization'] = 'Bearer ' + headerToken
        }

        return config;
    },
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    (response: AxiosResponse<any, any>) => {
        if (response.status >= 200 && response.status < 300) {
            return response;
        } else {
          return Promise.reject(response);
        }
    },
    (error: AxiosError<any, any>) => {
        console.log(error);
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            router.push('/login').catch(() => {});
        }
        ElMessage.error(error.response?.data?.message);
        return Promise.reject();
    }
);

export default service;
