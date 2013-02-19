<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head></head>
<body>
	<div id="container-signup">
	<form:form method="POST" modelAttribute="user" action="user/registration" id="signup">
				<div class="row-fluid">
					<div class="span10">
						<h3>Sign Up Now !</h3>
									<table>
										<tr>
											<td><form:label path="username">User Name</form:label>
											<form:input path="username" required="true" /></td>
										</tr>
										
										
										<tr>
											<td><input type="submit" value="SignUp" class="btn btn-large"/>
											</td>
										</tr>
									</table>
					</div>

					

				</div>
			</form:form>
	</div>
</body>
</html>