<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">
<link rel="stylesheet" href='<c:url value="/resources/css/site.css" />'>
<title>Comentario</title>
<script type="text/javascript">
function Popup()
{
 var A = document.getElementById("etiquetas2").value;  

var B = document.getElementById("etiquetas").value;

if (B == ""){
 
document.getElementById("etiquetas").value = A;
document.getElementById("etiquetas2").value = "";
}
else{
    
 var C = B;
 document.getElementById("etiquetas").value = C+";"+A;
 document.getElementById("etiquetas2").value = "";  
}


}
</script>
</head>
<body>
<div class="container">

      <div class="content">
        <div class="page-header">
          <h1>Nuestro blog de comentarios <small></small></h1>
          <h1><small>Ventana para la realizacion de comentarios</small></h1>
        </div>
        <div class="row">
          <div class="span10">
            <h2>Comentario</h2>
            <jsp:include page="_message.jsp"/>
			<div class="alert-message block-message info">
		        <p>
					Hola <strong>${SESSION_CURRENT_USER.nickname}</strong>, realiza tu comentario!
				</p>
	        </div>
	      
	<form action="<c:url value="comentarioVista"></c:url>" method="post">
	<fieldset>
		  <div class="clearfix">
                      <label for="stilos">        .  </label>
                      <div align="center" class="btn-group">
                        
                        <a class="btn" href="#">
                        <i class="align_left icon-white6"></i>
                        </a>
                        <a class="btn" href="#">
                        <i class="align_center icon-white7"></i>
                        </a>
                        <a class="btn" href="#">
                        <i class="align_right icon-white8"></i>
                        </a>
                        <a class="btn" href="#">
                        <i class="justify icon-white9"></i>
                        </a>
                    </div>
            <label for="message">Comentario:</label>
            <div class="input">
              <textarea id="message" name="message" rows="5" style="width: 400px;"></textarea>
            </div>
          </div>
            <div class="clearfix">
            <label for="etiquetas">Etiquetas:</label>
            <div class="input">
              <input id="etiquetas" type="text" name="etiquetas" value="" readonly="readonly">
            </div>
          </div>
            <div class="clearfix">
            <label for="etiquetas2">Agregar Etiquetas:</label>
            <div class="input">
              <input id="etiquetas2" type="text" name="etiquetas2" value="" > <input type="button" class="btn info" value="Agregar Etiqueta" onclick="Popup()">
            </div>
             
          </div>
            
          <div class="actions">
		    <input type="submit" class="btn primary" value="Enviar Comentario">
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