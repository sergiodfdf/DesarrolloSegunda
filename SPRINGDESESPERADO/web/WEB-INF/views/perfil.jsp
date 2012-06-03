<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
<link rel="stylesheet" href='<c:url value="/resources/css/site.css" />'>
<title>Perfil</title>
</head>
<body>

<div class="container">
      <div class="content">
        <div class="page-header">
          <h1>Nuestro blog de comentarios <small></small></h1>
          <h1><small>Perfil de usuario</small></h1>
        </div>
        <div class="row">
          <div class="span10">
            <h2>Register</h2>
			<jsp:include page="_message.jsp"/>
                        
                        <c:choose>
				<c:when test="${SESSION_CURRENT_USER != null}">
					<div class="alert-message block-message info">
				        <p>
							Hola <strong>${SESSION_CURRENT_USER.nickname}</strong>
							
							<a class="btn small" href="<c:url value="/logout"/>">Cerrar Sesion</a>
                                                        <a class="btn danger" href="<c:url value="/borrandoUsuario"/>">Borrar Usuario</a>
						</p>
			        </div>
					
				</c:when>
				<c:otherwise>
					<a class="btn primary" class="btn primary" href="<c:url value="/register"/>">Crear Cuenta</a> | <a class="btn primary" href="<c:url value="/loginVista"/>">Iniciar Sesion</a>	Para comentar!	
				</c:otherwise>
			</c:choose>
			
			<form action="perfil" method="post">
				<fieldset>
				  <div class="clearfix">
		            <label for="nombre">Nombre</label>
		            <div class="input">
                                <input id="nombre" type="text" name="nombre" value=${SESSION_CURRENT_USER.nombre}>
		            </div>
		          </div>
		          <div class="clearfix">
		            <label for="apellido">Apellido</label>
		            <div class="input">
		             <input id="apellido" type="text" name="apellido" value=${SESSION_CURRENT_USER.apellido}>
		            </div>
		          </div>
		          <div class="clearfix">
		            <label for="clave">Clave</label>
		            <div class="input">
		             <input id="clave" type="password" name="clave" value=${SESSION_CURRENT_USER.clave}>
		            </div>
		          </div>
                            <div class="clearfix">
		            <label for="correo">Correo</label>
		            <div class="input">
		             <input id="correo" type="text" name="correo" value=${SESSION_CURRENT_USER.correo}>
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="nickname">Nickname</label>
		            <div class="input">
                                <input id="nickname" type="text" name="nickname" value=${SESSION_CURRENT_USER.nickname} readonly="readonly">
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="fecha_nac">Fecha De Nacimiento</label>
		            <div class="input">
		             <input id="fecha_nac" type="text" name="fecha_nac" value=${SESSION_CURRENT_USER.fecha_nac}>
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="pais">Pais</label>
		            <div class="input">
		             <input id="pais" type="text" name="pais" value=${SESSION_CURRENT_USER.pais}>
		            </div>
                            </div>
                            <div class="clearfix">
		            <label for="biografia">Biografia</label>
		            <div class="input">
		             <input id="biografia" type="text" name="biografia" value=${SESSION_CURRENT_USER.biografia}>
		            </div>
                            </div>
                                    
			<div class="actions">
		            <input type="submit" class="btn primary" value="Actualizar">&nbsp;<button type="reset" class="btn">Cancel</button>
		        </div>
				
				</fieldset>
			</form>
	
          </div>
          <div class="span4">
            <h3>Menu</h3>
             <p>
				<a href='<c:url value="/home"/>' class="btn primary">home</a>
			</p>
                        <li>
                            <a href="#">
                                <img class="thumbnail" src="<c:url value="/resources/images/${SESSION_CURRENT_USER.nickname}.jpg"/>" alt="" height="100" width="100">
                            </a>
                        </li>
          </div>
        </div>
      </div>

      <jsp:include page="_footer.jsp"/>

    </div>
    
</body>
</html>