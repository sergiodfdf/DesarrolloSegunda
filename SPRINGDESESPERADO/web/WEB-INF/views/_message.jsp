<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${flash_message != null}">
	  <div class="alert-message success">
        <a class="close" href="#">�</a>
        <p><strong>Barbaro!</strong> ${flash_message}</p>
      </div>
</c:if>
<c:if test="${error != null}">
      <div class="alert-message error">
        <a class="close" href="#">�</a>
        <p><strong>Error!</strong> ${error } </p>
      </div>
</c:if>