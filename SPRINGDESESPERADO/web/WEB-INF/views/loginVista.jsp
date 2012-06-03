<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
<link rel="stylesheet" href='<c:url value="/resources/css/site.css" />'>
<title>Inicio de sesion</title>
</head>
<body>
	
<div class="container">

      <div class="content">
        <div class="page-header">
          <h1>Nuestro blog de comentarios <small></small></h1>
          <h1><small>Ventana de inicio de sesion</small></h1>
        </div>
        <div class="row">
          <div class="span10">
            <h2>Inicia Sesion</h2>
			<jsp:include page="_message.jsp"/>
			
			<form action="loginVista" method="post">
				<fieldset>
				  <div class="clearfix">
		            <label for="nickName">NickName</label>
		            <div class="input">
		             <input id="nickName" type="text" name="nickName">
		            </div>
		          </div>
		          <div class="clearfix">
		            <label for="clave">Clave</label>
		            <div class="input">
		             <input id="clave" type="password" name="clave">
		            </div>
		          </div>
				<div class="actions">
		            <input type="submit" class="btn primary" value="Iniciar Sesion">&nbsp;<button type="Reset" class="btn">Cancel</button>
		        </div>
				
				</fieldset>
			</form>
	
          </div>
          <div class="span4">
            <h3>Menu</h3>
             <p>
				<a href='<c:url value="/home"/>' class="btn primary">home</a>
			</p>
          </div>
        </div>
      </div>

      <jsp:include page="_footer.jsp"/>

    </div>
    
</body>
</html>