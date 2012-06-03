<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
<link rel="stylesheet" href='<c:url value="/resources/css/site.css" />'>
<title>Busqueda de Etiquetas</title>
</head>
<body>
<div class="container">

      <div class="content">
        <div class="page-header">
          <h1>Nuestro blog de Comentarios <small></small></h1>
          <h1><small>Ventana para la visualizacion de las Etiquetas</small></h1>
        </div>
        <div class="row">
          <div class="span10">
            <h2>Etiquetas del Sistema</h2>
            <jsp:include page="_message.jsp"/>
			<div class="alert-message block-message info">
		        <p>
					Hola <strong>${SESSION_CURRENT_USER.nickname}</strong>
					 
				</p>
	        </div>
	      
        <form name="probando">
	<fieldset>
	<div id="listaDeEtiquetas">
		<table class="bordered-table">
		<tr><th>#</th><th>Nombre</th><!--<th>Fecha Creacion</th>-->
		<c:forEach items="${listaDeEtiquetas}" varStatus="status" var="comments">
			<tr>
				<td>${status.index}</td><td>${comments.nombre}</td>
                                
			</tr>
		</c:forEach>
		</table>
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
