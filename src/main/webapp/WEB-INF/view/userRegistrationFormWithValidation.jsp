<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User Registration Form</title>
    </head>

    <style>
        .error {
            color:red
        }
    </style>

    <body>
        <table>
            <form:form action="create-user-with-validation" modelAttribute="user" method="POST">
                <tr>
                    <td>First name:</td>
                    <td><form:input path="firstName" /></td>
                    <td><form:errors path="firstName" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Last name:</td>
                    <td><form:input path="lastName" /></td>
                    <td><form:errors path="lastName" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>email:</td>
                    <td><form:input path="email" /></td>
                    <td><form:errors path="email" cssClass="error" /></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><form:input path="password" /></td>
                    <td><form:errors path="password" cssClass="error" /></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit" /></td>
                </tr>
            </form:form>
        </table>
    </body>
</html>