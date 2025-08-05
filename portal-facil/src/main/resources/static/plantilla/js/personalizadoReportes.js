//Buscar Rol
function setAccion(valor) {
    document.getElementById("accion").value = valor;
}

//$(document).ready(function () {
window.onload = function() {
	const token = $('meta[name="_csrf"]').attr('content');
	const header = $('meta[name="_csrf-header"]').attr('content');
 	const menuReporteId = $('#menuReporteId').val();	
		
    $('#fechaInicioInput').datepicker({
        format: 'DD/MM/YYYY', // o 'mm/dd/yyyy' según necesites
        autoHide: true
    });

    $('#fechaFinInput').datepicker({
        format: 'dd/mm/yyyy',
        autoHide: true
    });
    
    $('#iconFechaInicio').on('click', function (e) {
        e.preventDefault();
        setTimeout(function () {
            $('#fechaInicioInput').focus();
        }, 10);
    });

    $('#iconFechaFinal').on('click', function (e) {
        e.preventDefault();
        setTimeout(function () {
            $('#fechaFinInput').focus();
        }, 10);
    });
    
    
// Llamada AJAX para cargar los reportes 

    $.ajax({
  url: '/portal-facil/repEspeciales/reportes/cargarReportes',
  type: 'POST',
  dataType: 'json',
  data: {
    menuReporteId: menuReporteId
  },
  beforeSend: function(xhr) {
    xhr.setRequestHeader(header, token);
  },
  success: function (data) {
    const $select = $('#reportesCobranza');
    $select.empty().append('<option value="">-- Seleccione --</option>');

    $.each(data, function (index, item) {
      $select.append(
        $('<option>', {
          value: item.id,
          text: item.reporte,
          'data-id': item.id,
          'data-reporte': item.reporte,
          'data-tabla': item.tabla || '',
          'data-clave': item.clave || ''
        })
      );
    });

    // Destruir si ya existía y volver a inicializar
    if ($select.hasClass("select2-hidden-accessible")) {
      $select.select2('destroy');
    }

    $select.select2({
      width: '100%',
      placeholder: '-- Seleccione --',
      dropdownParent: $('#popupColumnas') // o el contenedor que tengas
    });
  },
  error: function () {
    Swal.fire({
      icon: 'error',
      title: 'Ocurrió un error',
      text: 'No se pudieron cargar los reportes. Por favor, intente nuevamente.',
      confirmButtonText: 'Aceptar'
    });
  }
});
    
    
 	cargarCartera();
	cargarClasificacionMembresia();
	cargarEmpleados();
	
	cargarEstados();
	cargarEstatusRecibos();
	cargarEstatusUnidades();
	cargarLocaciones();
	cargarPromotores();
	cargarSeriesRecibos();
	cargarTiposCuponesDescuentoPaq();
	cargarTiposProductos();
	cargarUsuarios();
	cargarDesarrollos();
	cargarTiposMovimiento();
    
    var ordenOriginalCartera = [];
    function cargarCartera() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroCartera',
        type: 'POST',
         beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#carteraDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalCartera = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar cartera:', error);
            //mostrarError('No se pudieron cargar cartera');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar cartera');

            
        }
    });
}

var ordenOriginalClasificacionMembresia  = [];
function cargarClasificacionMembresia() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroClasificacionMembresia',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#clasificacionMembresiaDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalClasificacionMembresia = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar clasificacion de membresia:', error);
          //  mostrarError('No se pudieron cargar clasificacion de membresia');
          mostrarAlerta('danger', 'Error', 'No se pudieron cargar clasificacion de membresia');
        }
    });
}

var ordenOriginalEmpleados  = [];
function cargarEmpleados() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroEmpleados',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#empleadosDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalEmpleados = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar empleados:', error);
            //mostrarError('No se pudieron cargar empleados');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar empleados');
        }
    });
}

var ordenOriginalEstados  = [];
function cargarEstados() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroEstados',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#estadosDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalEstados = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar estados:', error);
            //mostrarError('No se pudieron cargar estados');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar estados');
        }
    });
}

var ordenOriginalEstatusRecibos  = [];
function cargarEstatusRecibos() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroEstatusRecibos',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#estatusRecibosDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalEstatusRecibos = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar estatus recibos:', error);
            //mostrarError('No se pudieron cargar estatus recibos');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar estatus recibos');
        }
    });
}

var ordenOriginalEstatusUnidades  = [];
function cargarEstatusUnidades() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroEstatusUnidades',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#estatusUnidadesDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalEstatusUnidades = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar estatus unidades:', error);
            //mostrarError('No se pudieron cargar estatus unidades');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar estatus unidades');
        }
    });
}

var ordenOriginalLocaciones  = [];
function cargarLocaciones() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroLocaciones',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#locacionesDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalLocaciones = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar locaciones:', error);
            //mostrarError('No se pudieron cargar locaciones');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar locaciones');
        }
    });
}

var ordenOriginalPromotores  = [];
function cargarPromotores() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroPromotores',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#promotoresDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalPromotores = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar promotores:', error);
            //mostrarError('No se pudieron cargar promotores');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar promotores');
        }
    });
}

var ordenOriginalSeriesRecibos  = [];
function cargarSeriesRecibos() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroSeriesRecibos',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#seriesRecibosDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalSeriesRecibos = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar series recibos:', error);
            //mostrarError('No se pudieron cargar series recibos');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar series recibos');
        }
    });
}

var ordenOriginalTipoCuponesDescuentoPaq  = [];
function cargarTiposCuponesDescuentoPaq() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroTipoCuponesDescuentoPaq',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#tipoCuponesDescuentoPaqDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalTipoCuponesDescuentoPaq = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar tipo cupones descuento paq:', error);
            //mostrarError('No se pudieron cargar tipo cupones descuento paq');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar tipo cupones descuento paq');
        }
    });
}

var ordenOriginalTiposProductos  = [];
function cargarTiposProductos() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroTiposProductos',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#tiposProductosDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalTiposProductos = response.map(item => item.id);
            
            response.forEach(function(cartera) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(cartera.id)
                        .text(cartera.desc)
                        .data('descripcion', cartera.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar tipos productos:', error);
            //mostrarError('No se pudieron cargar tipos productos');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar tipos productos');
        }
    });
}

var ordenOriginalUsuarios = [];
function cargarUsuarios() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroUsuarios',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#usuariosDisponibles');
            selectDisponibles.empty();
            
            // Guardar el orden original
            ordenOriginalUsuarios = response.map(item => item.id);
            
            response.forEach(function(usuario) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(usuario.id)
                        .text(usuario.desc)
                        .data('descripcion', usuario.desc)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar usuarios:', error);
            //mostrarError('No se pudieron cargar usuarios');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar usuarios');
        }
    });
}

var ordenOriginalDesarrollos = [];
function cargarDesarrollos() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroDesarrollo',
        type: 'POST',
        dataType: 'json',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(data) {
            var selectDisponibles = $('#desarrollosDisponibles');
            selectDisponibles.empty();
            
            if(data.length === 0) {
                selectDisponibles.append('<option disabled>No hay desarrollos disponibles</option>');
                return;
            }
            
            // Ordenar los desarrollos por ID
            data.sort((a, b) => parseInt(a.id) - parseInt(b.id));
			
			// Guardar el orden original
			 ordenOriginalDesarrollos = data.map(item => item.id);
            
            // Agregar los elementos al select
            data.forEach(function(item) {
                selectDisponibles.append(
                    $('<option>', {
                        value: item.id,
                        text: `${item.id}-${item.desarrollo}`,
                        'data-tabla': item.tabla,
                        'data-clave': item.clave
                    })
                );
            });
            
            // Inicializar el primer elemento como seleccionado
            if(data.length > 0) {
                selectDisponibles.find('option:first').prop('selected', true);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar los desarrollos:', error);
            $('#desarrollosDisponibles').html('<option disabled>Error al cargar desarrollos</option>');
        }
    });
}

var ordenOriginalTiposMov = [];
function cargarTiposMovimiento() {
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/params/obtenerParametroTiposMovimiento',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        success: function(response) {
            const selectDisponibles = $('#tiposMovDisponibles');
            selectDisponibles.empty();
			
			// Guardar el orden original
			ordenOriginalTiposMov = response.map(item => item.id);
            
            response.forEach(function(tipoMov) {
                selectDisponibles.append(
                    $('<option></option>')
                        .val(tipoMov.id)
                        .text(tipoMov.desarrollo)
                        .data('descripcion', tipoMov.desarrollo)
                );
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar tipos de movimiento:', error);
            //mostrarError('No se pudieron cargar los tipos de movimiento');
            mostrarAlerta('danger', 'Error', 'No se pudieron cargar los tipos de movimiento');
        }
    });
}


//Botones de Cartera

 $('#btnAddCartera').click(function() {
    $('#carteraDisponibles option:selected').each(function() {
        $(this).remove().appendTo('#carteraSeleccionados');
        $(this).prop('selected', false);
    });
    ordenarSelect('#carteraSeleccionados');
});

$('#btnAddAllCartera').click(function() {
    $('#carteraDisponibles option').each(function() {
        $(this).remove().appendTo('#carteraSeleccionados');
        $(this).prop('selected', false);
    });
    ordenarSelect('#carteraSeleccionados');
});

$('#btnRemoveCartera').click(function() {
    $('#carteraSeleccionados option:selected').each(function() {
        var option = $(this).remove();
        insertarCarteraEnOrdenOriginal(option);
        option.prop('selected', false);
    });
});

$('#btnRemoveAllCartera').click(function() {
    $('#carteraSeleccionados option').each(function() {
        var option = $(this).remove();
        insertarCarteraEnOrdenOriginal(option);
        option.prop('selected', false);
    });
});

// Búsqueda en cartera
$('#buscarCartera').on('input', function() {
    filtrarCartera($(this).val());
});

$('#btnLimpiarBusquedaCartera').click(function() {
    $('#buscarCartera').val('');
    $('#carteraDisponibles option').show();
    
    // Reordenar según el orden original
    var container = $('#carteraDisponibles');
    var options = container.find('option').detach();
    
    ordenOriginalCartera.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            container.append(option);
        }
    });
    
    $('#carteraDisponibles option').prop('selected', false);
});

// Doble clic para mover elementos
$('#carteraDisponibles').dblclick(function() {
    $('#carteraDisponibles option:selected').each(function() {
        $(this).remove().appendTo('#carteraSeleccionados');
        $(this).prop('selected', false);
    });
    ordenarSelect('#carteraSeleccionados');
});

$('#carteraSeleccionados').dblclick(function() {
    $('#carteraSeleccionados option:selected').each(function() {
        var option = $(this).remove();
        insertarCarteraEnOrdenOriginal(option);
        option.prop('selected', false);
    });
});

// Función para insertar cartera en orden original
function insertarCarteraEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalCartera.indexOf(id);
    var inserted = false;
    
    $('#carteraDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalCartera.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#carteraDisponibles').append(option);
    }
}

// Función para filtrar cartera
function filtrarCartera(termino) {
    termino = termino.toLowerCase();
    
    $('#carteraDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original solo para los visibles
    var container = $('#carteraDisponibles');
    var options = container.find('option:visible').detach();
    
    // Ordenar las opciones visibles según el orden original
    var orderedOptions = [];
    ordenOriginalCartera.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    // Agregar las opciones ordenadas al contenedor
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
} 


// Eventos para clasificación de membresía (idénticos a cartera)
 $('#btnAddClasificacionMembresia').click(function() {
     $('#clasificacionMembresiaDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#clasificacionMembresiaSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#clasificacionMembresiaSeleccionados');
 });

 $('#btnAddAllClasificacionMembresia').click(function() {
     $('#clasificacionMembresiaDisponibles option').each(function() {
         $(this).remove().appendTo('#clasificacionMembresiaSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#clasificacionMembresiaSeleccionados');
 });

 $('#btnRemoveClasificacionMembresia').click(function() {
     $('#clasificacionMembresiaSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarClasificacionMembresiaEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllClasificacionMembresia').click(function() {
     $('#clasificacionMembresiaSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarClasificacionMembresiaEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Doble clic para mover elementos
$('#clasificacionMembresiaDisponibles').dblclick(function() {
    $('#clasificacionMembresiaDisponibles option:selected').each(function() {
        $(this).remove().appendTo('#clasificacionMembresiaSeleccionados');
        $(this).prop('selected', false);
    });
    ordenarSelect('#clasificacionMembresiaDisponibles');
});

$('#clasificacionMembresiaSeleccionados').dblclick(function() {
    $('#clasificacionMembresiaSeleccionados option:selected').each(function() {
        var option = $(this).remove();
        insertarClasificacionMembresiaEnOrdenOriginal(option);
        option.prop('selected', false);
    });
});
 
  // Búsqueda en clasificación de membresía
 $('#buscarClasificacionMembresia').on('input', function() {
     filtrarClasificacionMembresia($(this).val());
 });

 $('#btnLimpiarBusquedaClasificacionMembresia').click(function() {
     $('#buscarClasificacionMembresia').val('');
     $('#clasificacionMembresiaDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#clasificacionMembresiaDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalClasificacionMembresia.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#clasificacionMembresiaDisponibles option').prop('selected', false);
 });
 
 function insertarClasificacionMembresiaEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalClasificacionMembresia.indexOf(id);
    var inserted = false;
    
    $('#clasificacionMembresiaDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalClasificacionMembresia.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#clasificacionMembresiaDisponibles').append(option);
    }
}

// Función para filtrar clasificación de membresía
function filtrarClasificacionMembresia(termino) {
    termino = termino.toLowerCase();
    $('#clasificacionMembresiaDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#clasificacionMembresiaDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalClasificacionMembresia.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para empleados (idénticos a cartera)
 $('#btnAddEmpleado').click(function() {
     $('#empleadosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#empleadosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#empleadosSeleccionados');
 });

 $('#btnAddAllEmpleado').click(function() {
     $('#empleadosDisponibles option').each(function() {
         $(this).remove().appendTo('#empleadosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#empleadosSeleccionados');
 });

 $('#btnRemoveEmpleado').click(function() {
     $('#empleadosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEmpleadoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllEmpleado').click(function() {
     $('#empleadosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEmpleadoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en empleados
 $('#buscarEmpleados').on('input', function() {
     filtrarEmpleados($(this).val());
 });

 $('#btnLimpiarBusquedaEmpleados').click(function() {
     $('#buscarEmpleados').val('');
     $('#empleadosDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#empleadosDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalEmpleados.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#empleadosDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#empleadosDisponibles').dblclick(function() {
     $('#empleadosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#empleadosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#empleadosSeleccionados');
 });

 $('#empleadosSeleccionados').dblclick(function() {
     $('#empleadosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEmpleadoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });


// Función para insertar empleados en orden original
function insertarEmpleadoEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalEmpleados.indexOf(id);
    var inserted = false;
    
    $('#empleadosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalEmpleados.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#empleadosDisponibles').append(option);
    }
}

// Función para filtrar empleados
function filtrarEmpleados(termino) {
    termino = termino.toLowerCase();
    $('#empleadosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#empleadosDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalEmpleados.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para estados (idénticos a cartera)
 $('#btnAddEstado').click(function() {
     $('#estadosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#estadosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estadosSeleccionados');
 });

 $('#btnAddAllEstado').click(function() {
     $('#estadosDisponibles option').each(function() {
         $(this).remove().appendTo('#estadosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estadosSeleccionados');
 });

 $('#btnRemoveEstado').click(function() {
     $('#estadosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEstadoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllEstado').click(function() {
     $('#estadosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEstadoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en estados
 $('#buscarEstados').on('input', function() {
     filtrarEstados($(this).val());
 });

 $('#btnLimpiarBusquedaEstados').click(function() {
     $('#buscarEstados').val('');
     $('#estadosDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#estadosDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalEstados.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#estadosDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#estadosDisponibles').dblclick(function() {
     $('#estadosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#estadosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estadosSeleccionados');
 });

 $('#estadosSeleccionados').dblclick(function() {
     $('#estadosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEstadoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar estados en orden original
function insertarEstadoEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalEstados.indexOf(id);
    var inserted = false;
    
    $('#estadosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalEstados.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#estadosDisponibles').append(option);
    }
}

// Función para filtrar estados
function filtrarEstados(termino) {
    termino = termino.toLowerCase();
    $('#estadosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#estadosDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalEstados.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para estatus de recibos
 $('#btnAddEstatusRecibo').click(function() {
     $('#estatusRecibosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#estatusRecibosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estatusRecibosSeleccionados');
 });

 $('#btnAddAllEstatusRecibo').click(function() {
     $('#estatusRecibosDisponibles option').each(function() {
         $(this).remove().appendTo('#estatusRecibosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estatusRecibosSeleccionados');
 });

 $('#btnRemoveEstatusRecibo').click(function() {
     $('#estatusRecibosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEstatusReciboEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllEstatusRecibo').click(function() {
     $('#estatusRecibosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEstatusReciboEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en estatus de recibos
 $('#buscarEstatusRecibos').on('input', function() {
     filtrarEstatusRecibos($(this).val());
 });

 $('#btnLimpiarBusquedaEstatusRecibos').click(function() {
     $('#buscarEstatusRecibos').val('');
     $('#estatusRecibosDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#estatusRecibosDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalEstatusRecibos.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#estatusRecibosDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#estatusRecibosDisponibles').dblclick(function() {
     $('#estatusRecibosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#estatusRecibosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estatusRecibosSeleccionados');
 });

 $('#estatusRecibosSeleccionados').dblclick(function() {
     $('#estatusRecibosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEstatusReciboEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 arguments// Función para insertar estatus de recibos en orden original
function insertarEstatusReciboEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalEstatusRecibos.indexOf(id);
    var inserted = false;
    
    $('#estatusRecibosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalEstatusRecibos.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#estatusRecibosDisponibles').append(option);
    }
}

function filtrarEstatusRecibos(termino) {
    termino = termino.toLowerCase();
    $('#estatusRecibosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#estatusRecibosDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalEstatusRecibos.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para estatus de unidades
 $('#btnAddEstatusUnidad').click(function() {
     $('#estatusUnidadesDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#estatusUnidadesSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estatusUnidadesSeleccionados');
 });

 $('#btnAddAllEstatusUnidad').click(function() {
     $('#estatusUnidadesDisponibles option').each(function() {
         $(this).remove().appendTo('#estatusUnidadesSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estatusUnidadesSeleccionados');
 });

 $('#btnRemoveEstatusUnidad').click(function() {
     $('#estatusUnidadesSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEstatusUnidadEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllEstatusUnidad').click(function() {
     $('#estatusUnidadesSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEstatusUnidadEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en estatus de unidades
 $('#buscarEstatusUnidades').on('input', function() {
     filtrarEstatusUnidades($(this).val());
 });

 $('#btnLimpiarBusquedaEstatusUnidades').click(function() {
     $('#buscarEstatusUnidades').val('');
     $('#estatusUnidadesDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#estatusUnidadesDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalEstatusUnidades.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#estatusUnidadesDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#estatusUnidadesDisponibles').dblclick(function() {
     $('#estatusUnidadesDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#estatusUnidadesSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#estatusUnidadesSeleccionados');
 });

 $('#estatusUnidadesSeleccionados').dblclick(function() {
     $('#estatusUnidadesSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarEstatusUnidadEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar estatus de unidades en orden original
function insertarEstatusUnidadEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalEstatusUnidades.indexOf(id);
    var inserted = false;
    
    $('#estatusUnidadesDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalEstatusUnidades.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#estatusUnidadesDisponibles').append(option);
    }
}

// Función para filtrar estatus de unidades
function filtrarEstatusUnidades(termino) {
    termino = termino.toLowerCase();
    $('#estatusUnidadesDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#estatusUnidadesDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalEstatusUnidades.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para locaciones (similar a cartera)
 $('#btnAddLocacion').click(function() {
     $('#locacionesDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#locacionesSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#locacionesSeleccionados');
 });

 $('#btnAddAllLocacion').click(function() {
     $('#locacionesDisponibles option').each(function() {
         $(this).remove().appendTo('#locacionesSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#locacionesSeleccionados');
 });

 $('#btnRemoveLocacion').click(function() {
     $('#locacionesSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarLocacionEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllLocacion').click(function() {
     $('#locacionesSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarLocacionEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en locaciones
 $('#buscarLocaciones').on('input', function() {
     filtrarLocaciones($(this).val());
 });

 $('#btnLimpiarBusquedaLocaciones').click(function() {
     $('#buscarLocaciones').val('');
     $('#locacionesDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#locacionesDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalLocaciones.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#locacionesDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#locacionesDisponibles').dblclick(function() {
     $('#locacionesDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#locacionesSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#locacionesSeleccionados');
 });

 $('#locacionesSeleccionados').dblclick(function() {
     $('#locacionesSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarLocacionEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar locaciones en orden original
function insertarLocacionEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalLocaciones.indexOf(id);
    var inserted = false;
    
    $('#locacionesDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalLocaciones.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#locacionesDisponibles').append(option);
    }
}

// Función para filtrar locaciones
function filtrarLocaciones(termino) {
    termino = termino.toLowerCase();
    $('#locacionesDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#locacionesDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalLocaciones.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

 // Eventos para promotores (similar a cartera)
 $('#btnAddPromotor').click(function() {
     $('#promotoresDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#promotoresSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#promotoresSeleccionados');
 });

 $('#btnAddAllPromotor').click(function() {
     $('#promotoresDisponibles option').each(function() {
         $(this).remove().appendTo('#promotoresSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#promotoresSeleccionados');
 });

 $('#btnRemovePromotor').click(function() {
     $('#promotoresSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarPromotorEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllPromotor').click(function() {
     $('#promotoresSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarPromotorEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en promotores
 $('#buscarPromotores').on('input', function() {
     filtrarPromotores($(this).val());
 });

 $('#btnLimpiarBusquedaPromotores').click(function() {
     $('#buscarPromotores').val('');
     $('#promotoresDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#promotoresDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalPromotores.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#promotoresDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#promotoresDisponibles').dblclick(function() {
     $('#promotoresDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#promotoresSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#promotoresSeleccionados');
 });

 $('#promotoresSeleccionados').dblclick(function() {
     $('#promotoresSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarPromotorEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar promotores en orden original
function insertarPromotorEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalPromotores.indexOf(id);
    var inserted = false;
    
    $('#promotoresDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalPromotores.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#promotoresDisponibles').append(option);
    }
}

// Función para filtrar promotores
function filtrarPromotores(termino) {
    termino = termino.toLowerCase();
    $('#promotoresDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#promotoresDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalPromotores.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para series de recibos
 $('#btnAddSerieRecibo').click(function() {
     $('#seriesRecibosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#seriesRecibosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#seriesRecibosSeleccionados');
 });

 $('#btnAddAllSerieRecibo').click(function() {
     $('#seriesRecibosDisponibles option').each(function() {
         $(this).remove().appendTo('#seriesRecibosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#seriesRecibosSeleccionados');
 });

 $('#btnRemoveSerieRecibo').click(function() {
     $('#seriesRecibosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarSerieReciboEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllSerieRecibo').click(function() {
     $('#seriesRecibosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarSerieReciboEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en series de recibos
 $('#buscarSeriesRecibos').on('input', function() {
     filtrarSeriesRecibos($(this).val());
 });

 $('#btnLimpiarBusquedaSeriesRecibos').click(function() {
     $('#buscarSeriesRecibos').val('');
     $('#seriesRecibosDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#seriesRecibosDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalSeriesRecibos.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#seriesRecibosDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#seriesRecibosDisponibles').dblclick(function() {
     $('#seriesRecibosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#seriesRecibosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#seriesRecibosSeleccionados');
 });

 $('#seriesRecibosSeleccionados').dblclick(function() {
     $('#seriesRecibosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarSerieReciboEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar series de recibos en orden original
function insertarSerieReciboEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalSeriesRecibos.indexOf(id);
    var inserted = false;
    
    $('#seriesRecibosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalSeriesRecibos.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#seriesRecibosDisponibles').append(option);
    }
}

// Función para filtrar series de recibos
function filtrarSeriesRecibos(termino) {
    termino = termino.toLowerCase();
    $('#seriesRecibosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#seriesRecibosDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalSeriesRecibos.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

 // Eventos para tipos de cupones de descuento
 $('#btnAddTipoCuponDescuentoPaq').click(function() {
     $('#tipoCuponesDescuentoPaqDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#tipoCuponesDescuentoPaqSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tipoCuponesDescuentoPaqSeleccionados');
 });

 $('#btnAddAllTipoCuponDescuentoPaq').click(function() {
     $('#tipoCuponesDescuentoPaqDisponibles option').each(function() {
         $(this).remove().appendTo('#tipoCuponesDescuentoPaqSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tipoCuponesDescuentoPaqSeleccionados');
 });

 $('#btnRemoveTipoCuponDescuentoPaq').click(function() {
     $('#tipoCuponesDescuentoPaqSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarTipoCuponDescuentoPaqEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllTipoCuponDescuentoPaq').click(function() {
     $('#tipoCuponesDescuentoPaqSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarTipoCuponDescuentoPaqEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en tipos de cupones de descuento
 $('#buscarTipoCuponesDescuentoPaq').on('input', function() {
     filtrarTipoCuponesDescuentoPaq($(this).val());
 });

 $('#btnLimpiarBusquedaTipoCuponesDescuentoPaq').click(function() {
     $('#buscarTipoCuponesDescuentoPaq').val('');
     $('#tipoCuponesDescuentoPaqDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#tipoCuponesDescuentoPaqDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalTipoCuponesDescuentoPaq.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#tipoCuponesDescuentoPaqDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#tipoCuponesDescuentoPaqDisponibles').dblclick(function() {
     $('#tipoCuponesDescuentoPaqDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#tipoCuponesDescuentoPaqSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tipoCuponesDescuentoPaqSeleccionados');
 });

 $('#tipoCuponesDescuentoPaqSeleccionados').dblclick(function() {
     $('#tipoCuponesDescuentoPaqSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarTipoCuponDescuentoPaqEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
// Función para insertar tipos de cupones de descuento en orden original
function insertarTipoCuponDescuentoPaqEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalTipoCuponesDescuentoPaq.indexOf(id);
    var inserted = false;
    
    $('#tipoCuponesDescuentoPaqDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalTipoCuponesDescuentoPaq.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#tipoCuponesDescuentoPaqDisponibles').append(option);
    }
}

function filtrarTipoCuponesDescuentoPaq(termino) {
    termino = termino.toLowerCase();
    $('#tipoCuponesDescuentoPaqDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#tipoCuponesDescuentoPaqDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalTipoCuponesDescuentoPaq.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
} 

// Eventos para tipos de productos
 $('#btnAddTipoProducto').click(function() {
     $('#tiposProductosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#tiposProductosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tiposProductosSeleccionados');
 });

 $('#btnAddAllTipoProducto').click(function() {
     $('#tiposProductosDisponibles option').each(function() {
         $(this).remove().appendTo('#tiposProductosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tiposProductosSeleccionados');
 });

 $('#btnRemoveTipoProducto').click(function() {
     $('#tiposProductosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarTipoProductoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllTipoProducto').click(function() {
     $('#tiposProductosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarTipoProductoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en tipos de productos
 $('#buscarTiposProductos').on('input', function() {
     filtrarTiposProductos($(this).val());
 });

 $('#btnLimpiarBusquedaTiposProductos').click(function() {
     $('#buscarTiposProductos').val('');
     $('#tiposProductosDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#tiposProductosDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalTiposProductos.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#tiposProductosDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#tiposProductosDisponibles').dblclick(function() {
     $('#tiposProductosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#tiposProductosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tiposProductosSeleccionados');
 });

 $('#tiposProductosSeleccionados').dblclick(function() {
     $('#tiposProductosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarTipoProductoEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar tipos de productos en orden original
function insertarTipoProductoEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalTiposProductos.indexOf(id);
    var inserted = false;
    
    $('#tiposProductosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalTiposProductos.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#tiposProductosDisponibles').append(option);
    }
}

// Función para filtrar tipos de productos
function filtrarTiposProductos(termino) {
    termino = termino.toLowerCase();
    $('#tiposProductosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#tiposProductosDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalTiposProductos.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Eventos para usuarios
 $('#btnAddUsuario').click(function() {
     $('#usuariosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#usuariosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#usuariosSeleccionados');
 });

 $('#btnAddAllUsuario').click(function() {
     $('#usuariosDisponibles option').each(function() {
         $(this).remove().appendTo('#usuariosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#usuariosSeleccionados');
 });

 $('#btnRemoveUsuario').click(function() {
     $('#usuariosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarUsuarioEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 $('#btnRemoveAllUsuario').click(function() {
     $('#usuariosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarUsuarioEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

 // Búsqueda en usuarios
 $('#buscarUsuarios').on('input', function() {
     filtrarUsuarios($(this).val());
 });

 $('#btnLimpiarBusquedaUsuarios').click(function() {
     $('#buscarUsuarios').val('');
     $('#usuariosDisponibles option').show();
     
     // Reordenar según el orden original
     var container = $('#usuariosDisponibles');
     var options = container.find('option').detach();
     
     ordenOriginalUsuarios.forEach(function(id) {
         var option = options.filter('[value="' + id + '"]');
         if (option.length) {
             container.append(option);
         }
     });
     
     $('#usuariosDisponibles option').prop('selected', false);
 });

 // Doble clic para mover elementos
 $('#usuariosDisponibles').dblclick(function() {
     $('#usuariosDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#usuariosSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#usuariosSeleccionados');
 });

 $('#usuariosSeleccionados').dblclick(function() {
     $('#usuariosSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarUsuarioEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });
 
 // Función para insertar usuarios en orden original
function insertarUsuarioEnOrdenOriginal(option) {0
    var id = option.val();
    var indexOriginal = ordenOriginalUsuarios.indexOf(id);
    var inserted = false;
    
    $('#usuariosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalUsuarios.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#usuariosDisponibles').append(option);
    }
}

// Función para filtrar usuarios
function filtrarUsuarios(termino) {
    termino = termino.toLowerCase();
    $('#usuariosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#usuariosDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalUsuarios.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

//Eventos Desarrollo
$('#btnMoverDerecha').click(moverDerecha);
$('#btnMoverTodoDerecha').click(moverTodoDerecha);
$('#btnMoverIzquierda').click(moverIzquierda);
$('#btnMoverTodoIzquierda').click(moverTodoIzquierda);

// Doble clic para mover elementos
$('#desarrollosDisponibles').dblclick(moverDerecha);
$('#desarrollosSeleccionados').dblclick(moverIzquierda);

function moverDerecha() {
    $('#desarrollosDisponibles option:selected').each(function() {
        $(this).remove().appendTo('#desarrollosSeleccionados');
		$(this).prop('selected', false);
    });
}

function moverTodoDerecha() {
    $('#desarrollosDisponibles option').each(function() {
        $(this).remove().appendTo('#desarrollosSeleccionados');
		$(this).prop('selected', false);
    });
}

function moverIzquierda() {
    $('#desarrollosSeleccionados option:selected').each(function() {
        var option = $(this).remove();
        insertarEnOrdenOriginal(option);
		option.prop('selected', false);
    });
}

function moverTodoIzquierda() {
    $('#desarrollosSeleccionados option').each(function() {
        var option = $(this).remove();
        insertarEnOrdenOriginal(option);
		option.prop('selected', false);
    });
}

// Evento para el campo de búsqueda
$('#buscarDesarrollos').on('input', function() {
    filtrarDesarrollos($(this).val());
});

// Evento para el botón limpiar búsqueda
$('#btnLimpiarBusquedaDesarrollos').click(function() {
    $('#buscarDesarrollos').val('');
    $('#desarrollosDisponibles option').show();
    
    // Reordenar según el orden original
    var container = $('#desarrollosDisponibles');
    var options = container.find('option').detach();
    
    ordenOriginalDesarrollos.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            container.append(option);
        }
    });
	$('#desarrollosDisponibles option').prop('selected', false);
	    
});

function insertarEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalDesarrollos.indexOf(id);
    var inserted = false;
    
    $('#desarrollosDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalDesarrollos.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false; // Salir del bucle each
        }
    });
    
    // Si no se insertó antes de ningún elemento, agregar al final
    if (!inserted) {
        $('#desarrollosDisponibles').append(option);
    }
}

function filtrarDesarrollos(termino) {
    termino = termino.toLowerCase();
    
    // Mostrar/ocultar opciones según el filtro
    $('#desarrollosDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original solo para los visibles
    var container = $('#desarrollosDisponibles');
    var options = container.find('option:visible').detach();
    
    // Ordenar las opciones visibles según el orden original
    var orderedOptions = [];
    ordenOriginalDesarrollos.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    // Agregar las opciones ordenadas al contenedor
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

// Función para mover opciones seleccionadas Tipos de movimientos
				
$('#btnAddTipoMov').click(function() {
    $('#tiposMovDisponibles option:selected').each(function() {
        $(this).remove().appendTo('#tiposMovSeleccionados');
		$(this).prop('selected', false);
    });
    ordenarSelect('#tiposMovSeleccionados');
});

$('#btnAddAllTipoMov').click(function() {
    $('#tiposMovDisponibles option').each(function() {
        $(this).remove().appendTo('#tiposMovSeleccionados');
		$(this).prop('selected', false);
    });
    ordenarSelect('#tiposMovSeleccionados');
});

$('#btnRemoveTipoMov').click(function() {
    $('#tiposMovSeleccionados option:selected').each(function() {
        var option = $(this).remove();
        insertarTipoMovEnOrdenOriginal(option);
		option.prop('selected', false);
    });
});

$('#btnRemoveAllTipoMov').click(function() {
    $('#tiposMovSeleccionados option').each(function() {
        var option = $(this).remove();
        insertarTipoMovEnOrdenOriginal(option);
		option.prop('selected', false);
    });
});

// Doble clic para mover elementos
 $('#tiposMovDisponibles').dblclick(function() {
     $('#tiposMovDisponibles option:selected').each(function() {
         $(this).remove().appendTo('#tiposMovSeleccionados');
         $(this).prop('selected', false);
     });
     ordenarSelect('#tiposMovSeleccionados');
 });

 $('#tiposMovSeleccionados').dblclick(function() {
     $('#tiposMovSeleccionados option:selected').each(function() {
         var option = $(this).remove();
         insertarTipoMovEnOrdenOriginal(option);
         option.prop('selected', false);
     });
 });

// Agrega los eventos para el campo de búsqueda
$('#buscarTiposMov').on('input', function() {
    filtrarTiposMov($(this).val());
});

$('#btnLimpiarBusquedaTiposMov').click(function() {
    $('#buscarTiposMov').val('');
    $('#tiposMovDisponibles option').show();
    
    // Reordenar según el orden original
    var container = $('#tiposMovDisponibles');
    var options = container.find('option').detach();
    
    ordenOriginalTiposMov.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            container.append(option);
        }
    });
	
	$('#tiposMovDisponibles option').prop('selected', false);
});

// Función para insertar tipos de movimiento en orden original
function insertarTipoMovEnOrdenOriginal(option) {
    var id = option.val();
    var indexOriginal = ordenOriginalTiposMov.indexOf(id);
    var inserted = false;
    
    $('#tiposMovDisponibles option').each(function() {
        var currentId = $(this).val();
        var currentIndex = ordenOriginalTiposMov.indexOf(currentId);
        
        if (currentIndex > indexOriginal) {
            option.insertBefore($(this));
            inserted = true;
            return false;
        }
    });
    
    if (!inserted) {
        $('#tiposMovDisponibles').append(option);
    }
}

// Función para filtrar tipos de movimiento
function filtrarTiposMov(termino) {
    termino = termino.toLowerCase();
    $('#tiposMovDisponibles option').each(function() {
        var texto = $(this).text().toLowerCase();
        $(this).toggle(texto.includes(termino));
    });
    
    // Reordenar manteniendo el orden original
    var container = $('#tiposMovDisponibles');
    var options = container.find('option:visible').detach();
    
    var orderedOptions = [];
    ordenOriginalTiposMov.forEach(function(id) {
        var option = options.filter('[value="' + id + '"]');
        if (option.length) {
            orderedOptions.push(option);
        }
    });
    
    orderedOptions.forEach(function(option) {
        container.append(option);
    });
}

$('#btnLimpiarFiltros').click(function() {
	limpiarParametrosReporte();
});
function limpiarParametrosReporte() {
    // Limpiar o resetear los campos de parámetros
     $('#carteraSeleccionados option').each(function() {
        var option = $(this).remove();
        insertarCarteraEnOrdenOriginal(option);
    });
    
    $('#clasificacionMembresiaSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarClasificacionMembresiaEnOrdenOriginal(option);
     });
     
     $('#clasificacionMembresiaSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarClasificacionMembresiaEnOrdenOriginal(option);
     });
     
      $('#empleadosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEmpleadoEnOrdenOriginal(option);
     });
     
     $('#estadosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEstadoEnOrdenOriginal(option);
     });
     
     $('#estatusRecibosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEstatusReciboEnOrdenOriginal(option);
     });
     
     $('#estatusUnidadesSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarEstatusUnidadEnOrdenOriginal(option);
     });arguments
     
     $('#locacionesSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarLocacionEnOrdenOriginal(option);
     });
     
      $('#promotoresSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarPromotorEnOrdenOriginal(option);
     });
     
      $('#seriesRecibosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarSerieReciboEnOrdenOriginal(option);
     });
     
      $('#tipoCuponesDescuentoPaqSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarTipoCuponDescuentoPaqEnOrdenOriginal(option);
     });
     
     $('#tiposProductosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarTipoProductoEnOrdenOriginal(option);
     });
     
     $('#usuariosSeleccionados option').each(function() {
         var option = $(this).remove();
         insertarUsuarioEnOrdenOriginal(option);
     });

	$('#desarrollosSeleccionados option').each(function() {
	        var option = $(this).remove();
	        insertarEnOrdenOriginal(option);
	});
		
	$('#tiposMovSeleccionados option').each(function() {
	        var option = $(this).remove();
	        insertarTipoMovEnOrdenOriginal(option);
	});
		
	$('#btnLimpiarBusquedaDesarrollos').click();
	$('#btnLimpiarBusquedaTiposMov').click();
	$('#btnLimpiarBusquedaCartera').click();
	$('#btnLimpiarBusquedaClasificacionMembresia').click();
	$('#btnLimpiarBusquedaEmpleados').click();
	$('#btnLimpiarBusquedaEstados').click();
	$('#btnLimpiarBusquedaEstatusRecibos').click();
	$('#btnLimpiarBusquedaEstatusUnidades').click();
	$('#btnLimpiarBusquedaLocaciones').click();
	$('#btnLimpiarBusquedaPromotores').click();
	$('#btnLimpiarBusquedaSeriesRecibos').click();
	$('#btnLimpiarBusquedaTipoCuponesDescuentoPaq').click();
	$('#btnLimpiarBusquedaTiposProductos').click();
	$('#btnLimpiarBusquedaUsuarios').click();
	
	/*reiniciarFechaInicioDatepicker();
	reiniciarFechaFinalDatepicker();
	resetearListadoReportes();*/
}

 function ordenarSelect(selectId) {
    const select = $(selectId);
    const options = select.find('option');
    options.sort(function(a, b) {
        return $(a).text().localeCompare($(b).text());
    });
    select.empty().append(options);
}

function resetearListadoReportes() {
    const select = document.getElementById('reportesCobranza');
    if (select) {
        select.selectedIndex = 0; 
    }
}

function reiniciarFechaInicioDatepicker() {
	
    // Destruye la instancia si existe
    $('#fechaInicioInput').datepicker('destroy');

    // Limpia el valor del input
    $('#fechaInicioInput').val('');

    // Vuelve a crear el datepicker con las opciones deseadas
    $('#fechaInicioInput').datepicker({
        format: 'DD/MM/YYYY',
        autoHide: true
    });
}

function reiniciarFechaFinalDatepicker() {
	
    // Destruye la instancia si existe
    $('#fechaFinInput').datepicker('destroy');

    // Limpia el valor del input
    $('#fechaFinInput').val('');

    // Vuelve a crear el datepicker con las opciones deseadas
    $('#fechaFinInput').datepicker({
        format: 'DD/MM/YYYY',
        autoHide: true
    });
    
    
}

// Validación para que fecha fin no sea menor que fecha inicio
   /* $("#fechaInicioInput").on("change.datetimepicker", function (e) {
        $('#fechaFinInput').datetimepicker('minDate', e.date);
    });
    
    $("#fechaFinInput").on("change.datetimepicker", function (e) {
        $('#fechaInicioInput').datetimepicker('maxDate', e.date);
    });*/


 $('#fechaInicioInput, #fechaFinInput').on('change', function () {
    validarFechas();
});
function validarFechas() {
    // Obtén las fechas como strings
    
    let fechaInicioStr = $('#fechaInicioInput').val();
    let fechaFinalStr = $('#fechaFinInput').val();

    if (!fechaInicioStr || !fechaFinalStr) {
        // Si alguna está vacía, considera válido o muestra mensaje si quieres
        return true;
    }

    // Convierte las fechas a objetos Date
    // Dependiendo del formato, aquí se adapta la conversión
    // Asumiendo formato 'DD/MM/YYYY'
    function parseFecha(fechaStr) {
        const partes = fechaStr.split('/');
        if (partes.length !== 3) return null;
        // new Date(año, mes - 1, día)
        return new Date(parseInt(partes[2]), parseInt(partes[1]) - 1, parseInt(partes[0]));
    }

    let fechaInicio = parseFecha(fechaInicioStr);
    let fechaFinal = parseFecha(fechaFinalStr);

    if (!fechaInicio || !fechaFinal) {
        //alert("Formato de fecha inválido");
        mostrarAlerta('danger', 'Error', 'Formato de fecha inválido.');
        return false;
    }

    if (fechaFinal < fechaInicio) {
        //alert("La fecha final no puede ser menor que la fecha inicio.");
        mostrarAlerta('danger', 'Error', 'La fecha final no puede ser menor que la fecha inicio.');
        return false;
    }

    return true;
}

function mostrarAlerta(tipo, titulo, mensaje) {
  const iconClass = {
    success: 'pe-7s-check',
    danger: 'pe-7s-close-circle',
    warning: 'pe-7s-info',
    info: 'pe-7s-info'
  }[tipo] || 'pe-7s-info';

  const alertHtml = $(`
    <div class="alert alert-${tipo} border-${tipo} text-dark alert-dismissible fade show mt-4 position-fixed top-0 start-50 translate-middle-x shadow-lg rounded px-4 py-3" 
         style="z-index:1060; min-width: 300px; max-width: 90vw;" role="alert">
      <div class="d-flex justify-content-between align-items-center">
        <div class="d-flex align-items-center">
          <i class="${iconClass} me-2 fs-4"></i>
          <strong class="me-2">${titulo}:</strong>
          <span>${mensaje}</span>
        </div>
        <button type="button" class="btn border-0 bg-transparent text-dark fs-5 ms-3" data-bs-dismiss="alert" aria-label="Cerrar">&times;</button>
      </div>
    </div>
  `);

  $('body').append(alertHtml);

  alertHtml.on('closed.bs.alert', function() {
    // Aquí podrías hacer algo después que se cierra la alerta si quieres
  });

  // Opcional: auto cerrar después de 5 segundos
  setTimeout(() => alertHtml.alert('close'), 5000);
}

$('#reportesCobranza').change(function() {
				
    var selectedOption = $(this).find('option:selected');
	
	var id = selectedOption.val(); // o selectedOption.data('id')
	var nombreReporte = selectedOption.data('reporte');
	var tabla = selectedOption.data('tabla');
	var clave = selectedOption.data('clave');
	    
	console.log("ID:", id);
	console.log("Nombre del Reporte:", nombreReporte);
	console.log("Tabla:", tabla);
	console.log("Clave:", clave);
	
	if(id){
	obtenerParametrosReporte(id);
	}else{
		$('.card[data-order]').hide();
		//Limpiar parametros
		limpiarParametrosReporte();
	}
});

function obtenerParametrosReporte(reporteId) {
    // Mostrar loading si es necesario
    Swal.fire({
        title: 'Obteniendo parámetros...',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    // Llamada AJAX para obtener los parámetros del reporte
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/obtenerParametrosReporte', // Ajusta esta URL según tu backend
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        data: { reporteId: reporteId },
        success: function(response) {
            Swal.close();
            
            // Procesar la respuesta y actualizar la UI según los parámetros
            actualizarUIconParametros(response);
			setTimeout(ordenarTarjetas, 300);
        },
        error: function(xhr, status, error) {
            /*Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudieron cargar los parámetros del reporte'
            });*/
			mostrarAlerta('danger', 'Error', 'No se pudieron cargar los parámetros del reporte');            
            console.error('Error al obtener parámetros:', error);
        }
    });
}

function actualizarUIconParametros(parametros) {
    // Aquí puedes actualizar la interfaz según los parámetros recibidos
    // Por ejemplo, habilitar/deshabilitar ciertos campos, mostrar/ocultar secciones, etc.
    
    console.log('Parámetros del reporte:', parametros);
	limpiarParametrosReporte();
    // Ejemplo: Mostrar u ocultar secciones según los parámetros
    if (parametros.requiereDesarrollos) {
        $('.card-desarrollos').show();
		$('.card-desarrollos').attr('data-order', parametros.paramIDDesarrollos);
		$('#divisionDesarrollos').removeClass('d-none');
    } else {
        $('.card-desarrollos').hide();
        $('#divisionDesarrollos').addClass('d-none');
    }
    
    if (parametros.requiereTiposMovimientos) {
        $('.card-tipos-movimiento').show();
		$('.card-tipos-movimiento').attr('data-order', parametros.paramIDTiposMovimientos);
		$('#divisionTiposMovimientos').removeClass('d-none');
    } else {
        $('.card-tipos-movimiento').hide();
        $('#divisionTiposMovimientos').addClass('d-none');
        
    }
	
	if (parametros.requiereCarteras) {
	    $('.card-cartera').show();
		$('.card-cartera').attr('data-order', parametros.paramIDCarteras);
		$('#divisionCartera').removeClass('d-none');
	} else {
	    $('.card-cartera').hide();
	    $('#divisionCartera').addClass('d-none');
	}

	if (parametros.requiereClasificacionMembresia) {
	    $('.card-clasificacion-membresia').show();
		$('.card-clasificacion-membresia').attr('data-order', parametros.paramIDClasificacionMembresia);
		$('#divisionClasificacionMembresia').removeClass('d-none');
	} else {
	    $('.card-clasificacion-membresia').hide();
	    $('#divisionClasificacionMembresia').addClass('d-none');
	}
	
	if (parametros.requiereEmpleados) {
	    $('.card-empleados').show();
		$('.card-empleados').attr('data-order', parametros.paramIDEmpleados);
		$('#divisionEmpleados').removeClass('d-none');
	} else {
	    $('.card-empleados').hide();
	    $('#divisionEmpleados').addClass('d-none');
	}	
	
	if (parametros.requiereEstados) {
	    $('.card-estados').show();
		$('.card-estados').attr('data-order', parametros.paramIDEstados);
		$('#divisionEstados').removeClass('d-none');
	} else {
	    $('.card-estados').hide();
	    $('#divisionEstados').addClass('d-none');
	}	
	
	if (parametros.requiereEstatusRecibos) {
	    $('.card-estatus-recibos').show();
		$('.card-estatus-recibos').attr('data-order', parametros.paramIDEstatusRecibos);
		$('#divisionEstatusRecibos').removeClass('d-none');
	} else {
	    $('.card-estatus-recibos').hide();
	    $('#divisionEstatusRecibos').addClass('d-none');
	}	
	
	if (parametros.requiereEstatusUnidades) {
	    $('.card-estatus-unidades').show();
		$('.card-estatus-unidades').attr('data-order', parametros.paramIDEstatusUnidades);
		$('#divisionEstatusUnidades').removeClass('d-none');
	} else {
	    $('.card-estatus-unidades').hide();
	    $('#divisionEstatusUnidades').addClass('d-none');
	}	
	
	if (parametros.requiereLocaciones) {
	    $('.card-locaciones').show();
		$('.card-locaciones').attr('data-order', parametros.paramIDLocaciones);
		$('#divisionLocaciones').removeClass('d-none');
	} else {
	    $('.card-locaciones').hide();
	    $('#divisionLocaciones').addClass('d-none');
	}		
	
	if (parametros.requierePromotores) {
	    $('.card-promotores').show();
		$('.card-promotores').attr('data-order', parametros.paramIDPromotores);
		$('#divisionPromotores').removeClass('d-none');
	} else {
	    $('.card-promotores').hide();
	    $('#divisionPromotores').addClass('d-none');
	}	
	
	if (parametros.requiereSeriesRecibos) {
	    $('.card-series-recibos').show();
		$('.card-series-recibos').attr('data-order', parametros.paramIDSeriesRecibos);
		$('#divisionSeriesRecibos').removeClass('d-none');
	} else {
	    $('.card-series-recibos').hide();
	    $('#divisionSeriesRecibos').addClass('d-none');
	}		
	
	if (parametros.requiereTiposCuponesDescuentoPqa) {
	    $('.card-tipos-cupones-descuento-pqa').show();
		$('.card-tipos-cupones-descuento-pqa').attr('data-order', parametros.paramIDTiposCuponesDescuentoPqa);
		$('#divisionTiposCuponesDescPaq').removeClass('d-none');
	} else {
	    $('.card-tipos-cupones-descuento-pqa').hide();
	    $('#divisionTiposCuponesDescPaq').addClass('d-none');
	}	
	
	if (parametros.requiereTiposProductos) {
	    $('.card-tipos-productos').show();
		$('.card-tipos-productos').attr('data-order', parametros.paramIDTiposProductos);
		$('#divisionTiposProductos').removeClass('d-none');
	} else {
	    $('.card-tipos-productos').hide();
	    $('#divisionTiposProductos').addClass('d-none');
	}	
			
	if (parametros.requiereUsuarios) {
	    $('.card-usuarios').show();
		$('.card-usuarios').attr('data-order', parametros.paramIDUsuarios);
		$('#divisionUsuarios').removeClass('d-none');
	} else {
	    $('.card-usuarios').hide();
	    $('#divisionUsuarios').addClass('d-none');
	}		
	
	ordenarTarjetas();
	
	setTimeout(ordenarTarjetas, 100); 
	// Procesar la memoria técnica para seleccionar los filtros guardados
	    if (parametros.memoriaTecnica && parametros.memoriaTecnica.length > 0) {
	        procesarMemoriaTecnica(parametros.memoriaTecnica);
	    }
}

function ordenarTarjetas() {
    // Seleccionar el contenedor padre de forma más precisa
    var $container = $('section.content > .container-fluid');
    
    // Seleccionar solo las tarjetas visibles que tienen data-order
    var $cards = $container.find('.card[data-order]:visible').detach();
    
    // Ordenar asegurando que data-order es numérico
    $cards.sort(function(a, b) {
        return parseInt($(a).data('order')) - parseInt($(b).data('order'));
    });
    
    // Insertar después del card de filtros de búsqueda
    $container.find('.card.card-info').after($cards);
}

function procesarMemoriaTecnica(memoriaTecnica) {
	
    // Agrupar los filtros por idParametroReporte
    const filtrosPorParametro = {};
    
    memoriaTecnica.forEach(item => {
        if (!filtrosPorParametro[item.idParametroReporte]) {
            filtrosPorParametro[item.idParametroReporte] = [];
        }
        filtrosPorParametro[item.idParametroReporte].push(item.idFiltroParametro);
    });
    
    // Para cada grupo de filtros, seleccionar las opciones correspondientes
    Object.keys(filtrosPorParametro).forEach(idParametro => {
        const card = $(`.card[data-order="${idParametro}"]`);
        
        if (card.length) {
            const idsFiltros = filtrosPorParametro[idParametro];
            seleccionarFiltrosEnCard(card, idsFiltros);
        }
    });
}

function seleccionarFiltrosEnCard(card, idsFiltros) {
	
    const selectDisponibles = card.find('select[id$="Disponibles"]');
    const selectSeleccionados = card.find('select[id$="Seleccionados"]');
    
    // Si no hay select de disponibles, puede que sea un caso especial (como desarrollos)
    if (selectDisponibles.length === 0) {
        return;
    }
    
    // Buscar cada filtro en los disponibles y moverlo a seleccionados
    idsFiltros.forEach(idFiltro => {
        const option = selectDisponibles.find(`option[value="${idFiltro}"]`);
        
        if (option.length) {
            option.detach().appendTo(selectSeleccionados);
        }
    });
    
    // Ordenar los seleccionados alfabéticamente
	
   /* if (selectSeleccionados.length) {
        ordenarSelect('#' + selectSeleccionados.attr('id'));
    }*/
}

$('#btnBuscar').off('click').on('click', function() {
					    
   
	//var fechaInicio = $('#fechaInicioInput').datetimepicker('viewDate');
    //var fechaFin = $('#fechaFinInput').datetimepicker('viewDate');
    var reporteSeleccionado = $('#reportesCobranza').val();
	var fechaInicioVal = $('#fechaInicioInput').val();
	var fechaFinVal = $('#fechaFinInput').val();
	var nombreReporte = $('#reportesCobranza option:selected').data('reporte') || 'Reporte';
	// Validación básica
						
	if (!fechaInicioVal) {
       /* Swal.fire({
            icon: 'warning',
            title: 'Seleccione fecha de inicio',
            text: 'Por favor seleccione una fecha inicio para continuar'
        });*/
           mostrarAlerta('danger', 'Error', 'Por favor seleccione una fecha inicio para continuar');
        return;
    }
	
	if (!fechaFinVal) {
        /*Swal.fire({
            icon: 'warning',
            title: 'Seleccione fecha final',
            text: 'Por favor seleccione una fecha final para continuar'
        });*/
        mostrarAlerta('danger', 'Error', 'Por favor seleccione una fecha final para continuar');
        return;
    }
	
	if (!reporteSeleccionado) {
        /*Swal.fire({
            icon: 'warning',
            title: 'Seleccione un reporte',
            text: 'Por favor seleccione un reporte para continuar'
        });*/
        mostrarAlerta('danger', 'Error', 'Por favor seleccione un reporte para continuar');
        return;
    }
    // Mostrar loading
    Swal.fire({
        title: 'Generando reporte. Por favor espere',
       // html: 'Por favor espere',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
	
	
	//const formData1= new FormData();
	const parametrosReporte = obtenerParametrosTarjetasVisibles();
    //formData1.append('parametrosReporte', JSON.stringify(parametrosReporte));
    
	
	/*for (let pair of formData1.entries()) {
	    console.log(pair[0] + ': ' + pair[1]);
	}
	debugger*/
    // Crear FormData para enviar los parámetros
	
	var $btn = $(this);
	$btn.prop('disabled', true);
	try {
    const formData = new FormData();
    formData.append('fechaInicio', fechaInicioVal);
    formData.append('fechaFin', fechaFinVal);
    formData.append('reporteId', reporteSeleccionado);
	formData.append('nombreReporte', nombreReporte);
	formData.append('parametrosReporte', JSON.stringify(parametrosReporte));
    

    // Enviar solicitud
    $.ajax({
        url: '/portal-facil/repEspeciales/reportes/searchReport',
        type: 'POST',
        beforeSend: function(xhr) {
        xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
    },
        data: formData,
        processData: false,
        contentType: false,
       /* xhrFields: {
            responseType: 'blob' // Indicar que esperamos un archivo binario
        },*/
        success: function(data) {
			
            Swal.close();
			$btn.prop('disabled', false);
			// Mostrar modal con columnas disponibles
			
			           mostrarSelectorColumnas(data.columns, data.uuid);
        },
        error: function(xhr) {
			
            Swal.close();
			$btn.prop('disabled', false);
            if (xhr.status === 400) {
                // Error de validación
                /*Swal.fire({
                    icon: 'error',
                    title: 'Error en los parámetros',
                    text: xhr.responseText
                });*/
                mostrarAlerta('danger', 'Error', 'Error en los parámetros'); 
            } else {
                // Otro tipo de error
                /*Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Ocurrió un error al generar el reporte'
                });*/
                 mostrarAlerta('danger', 'Error', 'Ocurrió un error al generar el reporte'); 
            }
        },
		complete: function() {
		$btn.prop('disabled', false);
       }
    });
} catch (e) {
        $btn.prop('disabled', false);
        console.error('Error inesperado:', e);
       /* Swal.fire({
            icon: 'error',
            title: 'Error inesperado',
            text: 'Ocurrió un error inesperado al procesar la solicitud'
        });*/
        mostrarAlerta('danger', 'Error', 'Ocurrió un error inesperado al procesar la solicitud');
        throw e;
    }
});

function obtenerParametrosTarjetasVisibles() {
    const parametros = {};
    
    // Obtener todas las tarjetas visibles
    $('.card[data-order]:visible').each(function() {
		
        const card = $(this);
        //const cardClass = card.attr('class').split(' ').find(cls => cls.startsWith('card-'));
        
        // Obtener los valores seleccionados
        const seleccionados = [];
        const selectSeleccionados = card.find('select[id$="Seleccionados"]');
		var selectedAll =false;
		        
        if (selectSeleccionados.length > 0) {
            selectSeleccionados.find('option').each(function() {
                seleccionados.push({
                    //clave: $(this).data('clave') || undefined,
                    id: $(this).val(),
                    //tabla: $(this).data('tabla') || undefined,
                    valor: $(this).text()
					
                });
            });
			selectedAll = false;
        }
		
		if(seleccionados.length == 0){
			
			const selectDisponibles = card.find('select[id$="Disponibles"]');
			            if (selectDisponibles.length > 0) {
			                selectDisponibles.find('option').each(function() {
			                    seleccionados.push({
			                        id: $(this).val(),
			                        valor: $(this).text()
			                    });
			                });
			            }
						
						selectedAll = true;
		}
        
        // Agregar al objeto de parámetros
        parametros[card.data('order')] = {
            dataOrder: card.data('order'),
            seleccionados: seleccionados,
			selectedAll: selectedAll
        };
    });
    
    return parametros;
}

// Función para mostrar el selector de columnas
function mostrarSelectorColumnas(columnas, uuid) {
    
    if (!columnas || columnas.length === 0) {
       /* Swal.fire({
            icon: 'error',
            title: 'Error al generar reporte',
            text: 'No se pudieron obtener los campos del reporte',
            confirmButtonText: 'Aceptar'
        });*/
        mostrarAlerta('danger', 'Error', 'No se pudieron obtener los campos del reporte');
        return;
    }

    const $listaColumnas = $('#listaColumnas');
    $listaColumnas.empty();

    columnas.forEach(col => {
        $listaColumnas.append(`
            <tr>
                <td class="align-middle text-center">
                    <input type="checkbox" class="columna-check" 
                           id="col-${col.replace(/\W+/g, '-')}" 
                           checked data-columna="${col}"
                           style="width: 16px; height: 16px;">
                </td>
                <td class="align-middle">
                    <label for="col-${col.replace(/\W+/g, '-')}" style="cursor: pointer;">
                        ${col}
                    </label>
                </td>
            </tr>
        `);
    });

    // Mostrar el card popup en lugar del modal
    abrirPopup();

    $('#btnSeleccionarTodos').off('click').on('click', function () {
        $('.columna-check').prop('checked', true);
    });

    $('#btnDeseleccionarTodos').off('click').on('click', function () {
        $('.columna-check').prop('checked', false);
    });

    $('#buscarColumnas').off('input').on('input', function () {
        const termino = $(this).val().toLowerCase();
        $('#listaColumnas tr').each(function () {
            const texto = $(this).find('td:last').text().toLowerCase();
            $(this).toggle(texto.includes(termino));
        });
    });

    $('#btnLimpiarBusqueda').off('click').on('click', function () {
        $('#buscarColumnas').val('').trigger('input');
        $(this).blur();
    });

    $listaColumnas.off('click').on('click', 'tr', function (e) {
        if (!$(e.target).is('input')) {
            const $checkbox = $(this).find('.columna-check');
            $checkbox.prop('checked', !$checkbox.prop('checked')).trigger('change');
        }
    });

    let isDownloading = false;
    $('#btnGenerarReporte').off('click').on('click', function () {
        const $btn = $(this);
        $btn.prop('disabled', true);

        try {
            const columnasSeleccionadas = obtenerColumnasSeleccionadas();
            if (columnasSeleccionadas.length === 0) {
                $btn.prop('disabled', false);
                /*Swal.fire({
                    icon: 'warning',
                    title: 'Seleccione columnas',
                    text: 'Por favor seleccione al menos una columna para generar el reporte'
                });*/
                mostrarAlerta('danger', 'Error', 'Por favor seleccione al menos una columna para generar el reporte');
                return;
            }

            if (isDownloading) return;
            isDownloading = true;

            const fechaInicioVal = $('#fechaInicio').find('input').val();
            const fechaFinVal = $('#fechaFin').find('input').val();
            const reporteSeleccionado = $('#reportesCobranza').val();
            const nombreReporte = $('#reportesCobranza option:selected').data('reporte') || 'Reporte';
            const parametrosReporte = obtenerParametrosTarjetasVisibles();

            Swal.fire({
                title: 'Generando reporte. Por favor espere',
                //html: 'Por favor espere',
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });

            const formData = new FormData();
            formData.append('fechaInicio', fechaInicioVal);
            formData.append('fechaFin', fechaFinVal);
            formData.append('reporteId', reporteSeleccionado);
            formData.append('nombreReporte', nombreReporte);
            formData.append('parametrosReporte', JSON.stringify(parametrosReporte));
            formData.append('columnasSeleccionadas', JSON.stringify(columnasSeleccionadas));
            formData.append('uuid', uuid);

            $.ajax({
                url: '/portal-facil/repEspeciales/reportes/generarReporte',
                type: 'POST',
                beforeSend: function(xhr) {
				 xhr.setRequestHeader(header, token); // Aquí se agrega el token CSRF
				},
                data: formData,
                processData: false,
                contentType: false,
                xhrFields: {
                    responseType: 'blob'
                },
                success: function (data) {
                    Swal.close();

                    const now = new Date();
                    const pad = (num, size) => num.toString().padStart(size, '0');
                    const fecha = `${now.getFullYear()}${pad(now.getMonth() + 1, 2)}${pad(now.getDate(), 2)}`;
                    const hora = `${pad(now.getHours(), 2)}${pad(now.getMinutes(), 2)}${pad(now.getSeconds(), 2)}${pad(now.getMilliseconds(), 3)}`;
                    const timestamp = `${fecha}-${hora}`;

                    setTimeout(() => {
                        const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
                        const link = document.createElement('a');
                        link.href = window.URL.createObjectURL(blob);
                        link.download = nombreReporte + ' ' + timestamp + '.xlsx';

                        if (typeof link.click === 'function') {
                            document.body.appendChild(link);
                            link.click();

                            setTimeout(() => {
                                document.body.removeChild(link);
                                window.URL.revokeObjectURL(link.href);
                            }, 100);
                        } else {
                            window.open(link.href, '_blank');
                        }

                        $btn.prop('disabled', false);
                        cerrarPopup();
                    }, 1000);
                },
                error: function (xhr) {
                    Swal.close();
                    $btn.prop('disabled', false);
                    if (xhr.status === 400) {
                        /*Swal.fire({
                            icon: 'error',
                            title: 'Error en los parámetros',
                            text: xhr.responseText
                        });*/
                        mostrarAlerta('danger', 'Error', xhr.responseText);
                    } else {
                        /*Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Ocurrió un error al generar el reporte'
                        });*/
                        mostrarAlerta('danger', 'Error', 'Ocurrió un error al generar el reporte');
                    }
                },
                complete: function () {
                    $btn.prop('disabled', false);
                }
            });

        } catch (e) {
            $btn.prop('disabled', false);
            console.error('Error inesperado:', e);
            /*Swal.fire({
                icon: 'error',
                title: 'Error inesperado',
                text: 'Ocurrió un error inesperado al generar el reporte'
            });*/
            mostrarAlerta('danger', 'Error', 'Ocurrió un error inesperado al generar el reporte');
            throw e;
        }
    });
}



if ($('#reportesCobranza').hasClass('select2-hidden-accessible')) {
  $('#reportesCobranza').select2('destroy');
}

setTimeout(() => {
  $('#reportesCobranza').select2({
    width: '100%',
    placeholder: '-- Seleccione --'
  });
}, 200);

function obtenerColumnasSeleccionadas() {
	    const seleccionadas = [];
	    $('.columna-check:checked').each(function() {
	        seleccionadas.push($(this).data('columna'));
	    });
	    return seleccionadas;
	}

}

function abrirPopup() {
  $('#popupColumnas').fadeIn(200);
}

function cerrarPopup() {
  $('#popupColumnas').fadeOut(200);
}


