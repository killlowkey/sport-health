// js-cookie
import Cookies from 'js-cookie'

/*
* 设置cookies
* */
function getCookie (key) {
    return Cookies.get(key)
 }
 /*
 * 设置Cookies
 * */
function setCookie (key, value, expiresTime) {
     console.log('设置：' + key + '-' + value)
    var seconds = expiresTime
    var expires = new Date(new Date() * 1 + seconds * 1000)
    return Cookies.set(key, value, { expires: expires })
 }
 
 /*
 * 移除Cookies
 * */
 function removeCookie (key) {
    return Cookies.remove(key)
 }

 export default {
    getCookie,
    setCookie,
    removeCookie
 }