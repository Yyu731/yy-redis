import axios from 'axios'
import { BASE_URL, TIMEOUT } from './config'

class YYRequest {
  constructor(baseURL, timeout=10000) {
    this.instance = axios.create({
      baseURL,
      timeout
    })

    this.instance.interceptors.request.use(config => {
      return config
    }, err => {
      return err
    })
    this.instance.interceptors.response.use(res => {
      return res
    }, err => {
      return err
    })
  }

  request(config) {
    return new Promise((resolve, reject) => {
      this.instance.request(config).then(res => {
        resolve(res.data)
      }).catch(err => {
        reject(err)
      })
    })
  }

  get(config) {
    return this.request({ ...config, method: "get" })
  }

  post(config) {
    return this.request({ ...config, method: "post" })
  }

  delete(config) {
    return this.request({ ...config, method: "delete" })
  }

  put(config) {
    return this.request({ ...config, method: "put" })
  }
}

export default new YYRequest(BASE_URL, TIMEOUT)


