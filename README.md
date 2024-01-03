### 사용 메뉴얼
> JAVA 17, Spring Boot 3.2.1
>  
> 빌드 후 localhost:8080/swagger-ui/index.html 으로 접속
> 
> view는 스웨거를 사용하였으며 각각의 API를 테스트 할 수 있습니다.
> 
> 각각의 API를 테스트 할 때는, 테스트 하고자 하는 API를 클릭하고, 우측 상단의 `Try it out` 버튼을 클릭하면 됩니다.


#### user-controller
*   GET /users
    * 더미로 등록된 5명의 user id와 name을 조회할 수 있다.
    * 응답으로 조회되는 id는 다른 API의 매개변수 {userId}로 활용한다.


#### todo-controller
* POST /todos
    *  todo list 조회 API
    *  특정 담당자, 특정 일자의 todo list를 중요도/우선순위로 정렬하여 조회할 수 있다.
    *  응답으로 조회되는 id는 다른 API의 매개변수 {todoId}로 활용한다.
* POST /todo
    * todo 생성 API
* POST /todo/{todoId}/priority
    * 우선순위를 변경하는 API
    * 변경하고자 하는 task의 id와 등급을 입력한다.
* PATCH /todo/{todoId}/status
    * 태스크의 status값을 다음 status값으로 변경하는 API
    * e.g) 대기 -> 진행중으로 변경
* PATCH /todo/{todoId}/cancel
    * 태스크의 status 값을 취소(CANCEL)로 변경하는 API
* DELETE /todo/{todoId}
    * 태스크를 삭제하는 API
* PATCH /todo/{todoId}/delegation/{userId}
    * 태스크 위임 API
    * {todoId} 태스크를 {userId} 에게 위임한다.
* PATCH /todo/{todoId}/rejection
    * 태스크 위임 거절 API
    * {todoId} 태스크를 위임 거절한다.