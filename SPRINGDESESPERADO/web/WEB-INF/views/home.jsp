<%@page import="prueba.modelo.Etiqueta"%>
<%@page import="java.util.List"%>
<%@page import="prueba.DAO.comentarioDAO"%>
<%@page import="prueba.DAO.comentarioDAOImpl"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
	<link rel="stylesheet" href='<c:url value="/resources/css/site.css" />'>     
        
        
</head>
<body>
<div class="container">

      <div class="content">
        <div class="page-header">
          <h1>Nuestro blog de comentarios <small></small></h1>
          <h1><small>Ventana inicial del gestor de comentarios</small></h1>
        </div>
        <div class="row">
          <div class="span10">
            <h2>Bienvenido</h2>
            	<jsp:include page="_message.jsp"/>
			<p>
                            <c:if test="${error != null}">
                                <div class="alert-message error">
                                    <a class="close" href="#">×</a>
                                    <p><strong>Error!</strong> ${error } </p>
                                </div>
                            </c:if>
			<c:choose>
				<c:when test="${SESSION_CURRENT_USER != null}">
					<div class="alert-message block-message info">
				        <p>
							Hola <strong>${SESSION_CURRENT_USER.nickname}</strong>
							
							<a class="btn small" href="<c:url value="/logout"/>">Cerrar Sesion</a>
                                                        <a class="btn small" href="<c:url value="/perfil"/>">Perfil</a>
                                                        <a href="#"><img class="thumbnail" src="<c:url value="/resources/images/${SESSION_CURRENT_USER.nickname}.jpg"/>" alt="" height="50" width="50"></a>
						</p>
			        </div>
					
					<p>
						<a class="btn primary" href="<c:url value="/comentarioVista"/>">Crear comentario</a>
						| <a class="btn primary" href="<c:url value="/comentarioConAdjunto"/>">Crear Comentario con adjunto</a>
						| <a class="btn primary" href="<c:url value="/comentariosDeUsuario"/>">Comentarios realizados</a>
                                                | <a class="btn primary" href="<c:url value="/verVideos"/>">Ver Videos</a>
					</p>
        <form action="<c:url value="comentariosDeRaiz"></c:url>" method="get" name="probando2">
	<fieldset>
	<div id="comentariosEnListaRaiz">
            <table class="bordered-table">
                    <tr><th>#</th><th>Contenido</th><!--<th>Fecha Creacion</th>--><th>Acciones</th></tr>
		<c:forEach items="${comentariosEnListaRaiz}" varStatus="status" var="commentsRaiz">
			<tr>
				<td>${status.index}</td><td>${commentsRaiz.mensaje}</td><!--<td>${commentsRaiz.fecha_creacion}</td>-->
                                <td><a class="btn primary" href="<c:url value="/replyDeUnComentario?messageee=${commentsRaiz.id}"/>">Reply</a></td>
                                <td><a class="btn success" href="<c:url value="/MeGusta?messageee=${commentsRaiz.id}"/>"><i class="thumbs_up icon-white2"></i></a></td>
                                <td><a class="btn info" href="<c:url value="/NoMeGusta?messageee=${commentsRaiz.id}"/>"><i class="thumbs_down icon-white3"></i></a></td>
                                <td><a class="btn primary" href="<c:url value="/replysDeComentario?messageee=${commentsRaiz.id}"/>">Reply's</a></td>
                                <td><a class="btn primary" href="<c:url value="/verPuntuacion?messageee=${commentsRaiz.id}"/>">Puntos</a></td>
			</tr>
                        
		</c:forEach>
		</table>
	</div>
	</fieldset>
	</form>
				</c:when>
				<c:otherwise>
					<a class="btn primary" class="btn primary" href="<c:url value="/register"/>">Crear Cuenta</a> | <a class="btn primary" href="<c:url value="/loginVista"/>">Iniciar Sesion</a>	Para comentar!
                                        <img height="420" src='<c:url value="/resources/css/homeimage.jpg"/>'/>
				</c:otherwise>
			</c:choose>
                                        
			</p>
			
			<p style="display:none;"><a class="btn primary" href='<c:url value="/compare?input1=S&input2=B"></c:url>'>Compare S &amp; B</a></p>
          </div>
          <div class="span4">
            <h3>Menu</h3>
            <p>
				<a href='<c:url value="/home"/>' class="btn primary">home</a>
			</p>
                        <c:choose>
                        <c:when test="${SESSION_CURRENT_USER != null}">
                        <p>
                            <select style="width: 150px" name="forma" ONCHANGE="location = this.options[this.selectedIndex].value;">
                                        <option>Busqueda General:</option>
                                        <option value="/SPRINGDESESPERADO/buscandoUsuario">Buscar Usuarios</option>
                                        <option value="/SPRINGDESESPERADO/buscandoComentario">Buscar Comentarios</option>
                                        <option value="/SPRINGDESESPERADO/buscandoEtiqueta">Buscar Etiquetas</option>
                            </select>
                        </p>   
                        </c:when>
                        </c:choose> 
          </div>
        </div>
      </div>
      
		<jsp:include page="_footer.jsp"/>
      
    </div> <!-- /container -->
    
</body>
</html>
