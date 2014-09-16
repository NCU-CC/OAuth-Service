
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Auth Demo Page</title>
    <style type="text/css">
        .validateDiv {
            margin-top: 40%;
            border: 3px #369 solid;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="validateDiv">
        <form action="redirect" method="GET" >
            ${it.clientID} requests the permission of ${it.scope}
            <input type="hidden" name="state"  value=${it.state}>
                <br>
            <input type="submit" value="Agree" />
        </form>
    </div>
</body>
</html>
