package tw.edu.ncu.cc.oauth.resource.component

import tw.edu.ncu.cc.oauth.data.v1.management.resource.TokenRequestMetaObject

import javax.servlet.http.HttpServletRequest


interface TokenMetaDecider {
    TokenRequestMetaObject decide( HttpServletRequest request )
}