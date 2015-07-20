package tw.edu.ncu.cc.oauth.server.helper

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import tw.edu.ncu.cc.oauth.data.v1.message.IndexObject


class ResponseHelper {

    static <T> IndexObject index( T[] resources, Page page ) {
        def indexObject = new IndexObject<T>()
        if( page.hasPrevious() ) {
            Pageable previousPageable = page.previousPageable()
            indexObject.previous = new IndexObject.PageObject(
                    page: previousPageable.pageNumber,
                    size: previousPageable.pageSize
            )
        }
        if( page.hasNext() ) {
            Pageable nextPageable = page.nextPageable()
            indexObject.next = new IndexObject.PageObject(
                    page: nextPageable.pageNumber,
                    size: nextPageable.pageSize
            )
        }
        indexObject.data = resources
        indexObject
    }

}
