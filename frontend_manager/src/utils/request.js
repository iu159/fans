import axios from 'axios';
import {notify} from "@/components/NotificationPlugin/index"

axios.defaults.timeout = 5000;
axios.defaults.baseURL = 'http://localhost:8888';
axios.defaults.withCredentials = true;



//http request 拦截器
axios.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    console.log(error)
    return error;
  }
);


//http response 拦截器
axios.interceptors.response.use(
  response => {
    // if(response.data.errCode ==2){
    //   router.push({
    //     path:"/login",
    //     query:{redirect:router.currentRoute.fullPath}//从哪个页面跳转
    //   })
    // }
    if (response.status == 200) {
      return response;
    } else {
      notify({
        type: 'danger',
        title: response.data.message
      })
    }
  },
  async error => {
    let messages = error.response.data.message.split("-")
    for (let message of messages) {
      await notify({
        type: 'danger',
        title: message
      })
    }
    return Promise.reject(error.response.status)
  }
)


/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

export function get(url, params = {}) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
        params: params
      })
      .then(response => {
        resolve(response.data);
      })
      .catch(err => {
        reject(err)
      })
  })
}


/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function post(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, data)
      .then(response => {
        resolve(response);
      }, err => {
        reject(err)
      })
  })
}

/**
 * 封装patch请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function patch(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.patch(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}

/**
 * 封装delete请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function remove(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}

/**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */

export function put(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data)
      .then(response => {
        resolve(response.data);
      }, err => {
        reject(err)
      })
  })
}