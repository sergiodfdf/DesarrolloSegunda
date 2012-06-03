<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
<link rel="stylesheet" href='<c:url value="/resources/css/site.css" />'>
<title>Registrate</title>
</head>
<body>

<div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Nuestro blog de comentarios <small></small></h1>
          <h1><small>Registro de usuario</small></h1>
        </div>
        <div class="row">
          <div class="span10">
            <h2>Register</h2>
			<jsp:include page="_message.jsp"/>
			
			<form action="register" method="post">
				<fieldset>
				  <div class="clearfix">
		            <label for="nombre">Nombre</label>
		            <div class="input">
                                <input id="nombre" type="text" name="nombre">
		            </div>
		          </div>
		          <div class="clearfix">
		            <label for="apellido">Apellido</label>
		            <div class="input">
		             <input id="apellido" type="text" name="apellido">
		            </div>
		          </div>
		          <div class="clearfix">
		            <label for="clave">Clave</label>
		            <div class="input">
		             <input id="clave" type="password" name="clave">
		            </div>
		          </div>
                            <div class="clearfix">
		            <label for="correo">Correo</label>
		            <div class="input">
		             <input id="correo" type="text" name="correo">
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="nickname">Nickname</label>
		            <div class="input">
                                <input id="nickname" type="text" name="nickname">
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="fecha_nac">Fecha De Nacimiento</label>
		            <div class="input">
		             <input id="fecha_nac" type="text" name="fecha_nac">
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="pais">Pais</label>
		            <div class="input">
		             <input id="pais" type="text" name="pais">
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="biografia">Biografia</label>
		            <div class="input">
		             <input id="biografia" type="text" name="biografia">
		            </div>
                            </div>
                            <div class="clearfix">
		            <p>
                                Elegir foto a adjuntar:<br>
                                <input type="file" name="foto" size="40">
                            </p>
                            </div>
                                    
			<div class="actions">
		            <input type="submit" class="btn primary" value="Register">&nbsp;<button type="reset" class="btn">Cancel</button>
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