package tw.edu.ncu.cc.oauth.data.v1.message


class IndexObject< T > {

    static class PageObject {
        def Integer page
        def Integer size
    }

    def PageObject previous
    def PageObject next

    def T[] data

}
