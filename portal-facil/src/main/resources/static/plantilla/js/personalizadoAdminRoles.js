//Buscar Rol
function setAccion(valor) {
    document.getElementById("accion").value = valor;
}

//Habilitar / Deshabilitar
let estadoCambioData = {}; // Guardará datos temporales

function cambiarEstado(button) {
    const rolId = $(button).closest('tr').find('td:nth-child(1)').text();
    const rolNombre = $(button).closest('tr').find('td:nth-child(2)').text();
    const estaActivo = $(button).data('activo') === true;

    // Rellenar card
    $('#cardPopupTitulo').text(estaActivo ? '¿Desactivar rol?' : '¿Activar rol?');
    $('#cardPopupTexto').html(`¿Estás seguro de querer <b>${estaActivo ? 'desactivar' : 'activar'}</b> el rol <b>${rolNombre}</b>?`);

    // Mostrar card
    $('#cardBackdrop').fadeIn(0);
    $('#cardPopupConfirmacion').fadeIn(200);

    // Quitar eventos anteriores por seguridad
    $('#cardPopupConfirmar').off('click');
    $('#cardPopupCancelar').off('click');

    // Confirmar acción
    $('#cardPopupConfirmar').on('click', function () {
        const csrfToken = $('meta[name="_csrf"]').attr('content');

        $.ajax({
            url: '/portal-facil/usuarios/role/cambiarEstadoRol',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                rolId: parseInt(rolId),
                nuevoEstado: !estaActivo
            }),
            headers: {
                'X-CSRF-TOKEN': csrfToken
            },
            success: function (response) {
				debugger
                $('#cardPopupConfirmacion').fadeOut(200);
               
                /*const alert = `
                    <div class="position-fixed top-0 start-50 translate-middle-x mt-4 alert ${response.success ? 'alert-success' : 'alert-danger'} alert-dismissible fade show shadow" style="z-index: 1060;" role="alert">
                        ${response.message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
                    </div>`;*/
              /*const alert = $(`
					  <div class="alert alert-success alert-dismissible fade show mt-4 position-fixed top-0 start-50 translate-middle-x shadow-lg border border-success rounded px-4 py-3" 
					       style="z-index:1060; min-width: 300px; max-width: 90vw;" 
					       role="alert">
					       
					    <div class="d-flex justify-content-between align-items-center">
					      <div>
					        <i class="pe-7s-check text-white me-2"></i>
					        <span>${response.message}</span>
					      </div>
					      <button type="button" class="btn border-0 bg-transparent text-dark fs-5 ms-3" data-bs-dismiss="alert" aria-label="Cerrar">
							  &times;
							</button>
					    </div>
					  </div>
					`);*/
					
					const alert = $(`
					  <div class="alert ${response.success ? 'alert-success border-success text-dark' : 'alert-danger border-danger text-dark'} 
					              alert-dismissible fade show mt-4 position-fixed top-0 start-50 translate-middle-x 
					              shadow-lg rounded px-4 py-3"
					       style="z-index:1060; min-width: 300px; max-width: 90vw;" 
					       role="alert">
					    
					    <div class="d-flex justify-content-between align-items-center">
					      <div class="d-flex align-items-center">
					        <i class="${response.success ? 'pe-7s-check' : 'pe-7s-close-circle'} me-2 fs-4"></i>
					        <span>${response.message}</span>
					      </div>
					      <button type="button" class="btn border-0 bg-transparent text-dark fs-5 ms-3" data-bs-dismiss="alert" aria-label="Cerrar">
					        &times;
					      </button>
					    </div>
					  </div>
					`);

                $('body').append(alert);
                // <button type="button" class="btn-close btn-close-black ms-3" data-bs-dismiss="alert" aria-label="Cerrar"></button>
               /* if (response.success) {
                    setTimeout(() => location.reload(), 1500);
                }*/
                alert.on('closed.bs.alert', function () {
					 $('#cardBackdrop').fadeOut(0);
					  location.reload();
					});
            },
            error: function () {
				debugger
                $('#cardPopupConfirmacion').fadeOut(200);
                 $('#cardBackdrop').fadeOut(0);
                const alert = `
                    <div class="position-fixed top-0 start-50 translate-middle-x mt-4 alert alert-danger alert-dismissible fade show shadow" style="z-index: 1060;" role="alert">
                        No se pudo cambiar el estado del rol.
                        <button type="button" class="btn border-0 bg-transparent text-dark fs-5 ms-3" data-bs-dismiss="alert" aria-label="Cerrar">
                    </div>`;
                $('body').append(alert);
            }
        });
    });

    // Cancelar
    $('#cardPopupCancelar').on('click', function () {
        $('#cardPopupConfirmacion').fadeOut(200);
        $('#cardBackdrop').fadeOut(0);
    });
}

function confirmarGuardado() {
    const rolInput = document.getElementById('rol');
    const rolValue = rolInput.value.trim();

    // Validación: campo vacío
    if (!rolValue) {
        const alert = $(`
          <div class="alert alert-warning alert-dismissible fade show mt-4 position-fixed top-0 start-50 translate-middle-x shadow-lg border border-warning rounded px-4 py-3"
               style="z-index:1060; min-width: 300px; max-width: 90vw;" 
               role="alert">
            <div class="d-flex justify-content-between align-items-center">
              <div class="d-flex align-items-center">
                <i class="pe-7s-attention me-2 fs-4 text-warning"></i>
                <span>No se puede guardar un rol sin nombre. Por favor ingresa un nombre para el rol.</span>
              </div>
              <button type="button" class="btn border-0 bg-transparent text-dark fs-5 ms-3" data-bs-dismiss="alert" aria-label="Cerrar">&times;</button>
            </div>
          </div>
        `);
        $('body').append(alert);
        rolInput.focus();
        return;
    }

    // Mostrar card de confirmación
    $('#cardPopupTitulo').text('¿Guardar Rol?');
    $('#cardPopupTexto').text('¿Estás seguro de que deseas guardar este nuevo rol?');

    $('#cardBackdrop').fadeIn(0);
    $('#cardPopupConfirmacion').fadeIn(200);

    // Limpia eventos anteriores
    $('#cardPopupConfirmar').off('click');
    $('#cardPopupCancelar').off('click');

    // Confirmar
    $('#cardPopupConfirmar').on('click', function () {
        $('#cardPopupConfirmacion').fadeOut(200);
        $('#cardBackdrop').fadeOut(0);
        document.getElementById('accion').value = 'saveRoles';
        document.getElementById('rolForm').submit();
    });

    // Cancelar
    $('#cardPopupCancelar').on('click', function () {
        $('#cardPopupConfirmacion').fadeOut(200);
        $('#cardBackdrop').fadeOut(0);
    });
}


//Herencia


$(document).on('click', '.btn-herencia', function(e) {
  //  e.preventDefault();
   // e.stopPropagation();

    const rolId = $(this).closest('tr').find('td:nth-child(1)').text();
    const rolNombre = $(this).closest('tr').find('td:nth-child(2)').text();

    // Configurar popup
    $('#rolIdHerencia').val(rolId);
    $('#rolNombreActual').val(rolNombre);
    $('#cardHerenciaPopup .card-title:first').text(`Herencia de permisos para ${rolNombre}`);

    // Limpiar campos
    $('#rolPadre').val('');
    $('#seccionModulos').hide();

    // Cargar roles disponibles
    cargarRolesDisponibles(rolId);

    // Mostrar card
   // $('#cardBackdropHerencia').fadeIn(0);
    //$('#cardHerenciaPopup').fadeIn(200);
    mostrarCardHerencia();
});

function cargarRolesDisponibles(rolActualId) {
    $('#rolPadre').html('<option value="">Cargando roles...</option>');

    $.ajax({
        url: '/portal-facil/usuarios/loadRolesJson',
        type: 'POST',
        dataType: 'json',
        data: {
            _csrf: $('input[name="_csrf"]').val()
        },
        success: function(roles) {
            const $select = $('#rolPadre');
            $select.empty();
            $select.append('<option value="">Seleccione un rol...</option>');

            roles.forEach(rol => {
                if (rol.id && rol.nombre && rol.id !== rolActualId) {
                    $select.append(`<option value="${rol.id}">${rol.nombre}</option>`);
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('Error al cargar roles:', error);
            $('#rolPadre').html('<option value="">Error al cargar roles</option>');

            // Mostrar alerta personalizada tipo ArchitectUI
            const alert = $(`
              <div class="alert alert-danger alert-dismissible fade show mt-4 position-fixed top-0 start-50 translate-middle-x shadow-lg border border-danger rounded px-4 py-3"
                   style="z-index:1060; min-width: 300px; max-width: 90vw;" 
                   role="alert">
                <div class="d-flex justify-content-between align-items-center">
                  <div class="d-flex align-items-center">
                    <i class="pe-7s-close-circle text-danger me-2 fs-4"></i>
                    <span>No se pudieron cargar los roles disponibles.</span>
                  </div>
                  <button type="button" class="btn border-0 bg-transparent text-dark fs-5 ms-3" data-bs-dismiss="alert" aria-label="Cerrar">&times;</button>
                </div>
              </div>
            `);
            $('body').append(alert);
        }
    });
}

function mostrarCardHerencia() {
  $('#cardBackdropHerencia').fadeIn(0);
  $('#cardHerenciaPopup').fadeIn(200);
  $('body').addClass('no-scroll');
  
  $('#rolPadre').val('');
  $('#seccionModulos').hide();
  $('#guardarHerencia').prop('disabled', true);
    
  // Limpiar el árbol de módulos
  $('#modulosTree').html('<div class="text-center p-3 text-muted">Seleccione un rol para ver los módulos</div>');
}

function cerrarCardHerencia() {
  $('#cardHerenciaPopup').fadeOut(200);
  $('#cardBackdropHerencia').fadeOut(0);
   $('body').removeClass('no-scroll');
}

$(document).on('click', '#cerrarCardHerencia', cerrarCardHerencia);
$(document).on('click', '#cancelarCardHerencia', cerrarCardHerencia);
$(document).on('click', '#cardBackdropHerencia', cerrarCardHerencia);


$(document).on('click', '#guardarHerencia', function(e) {
  //e.preventDefault();

  const rolId = $('#rolIdHerencia').val();
  const rolNombreActual = $('#rolNombreActual').val();
  const rolPadreId = $('#rolPadre').val();
  const rolPadreNombre = $('#rolPadre option:selected').text();

  // Validación básica
  if (!rolPadreId) {
    mostrarAlerta('warning', 'Selección requerida', 'Por favor selecciona un rol del cual heredar permisos');
    return;
  }

  // Obtener módulos seleccionados
  const modulosSeleccionados = [];
  $('#modulosTree .modulo-checkbox:checked').each(function() {
    modulosSeleccionados.push(parseInt($(this).data('id')));
  });

  if (modulosSeleccionados.length === 0) {
    mostrarAlerta('danger', 'Error', 'Debe seleccionar al menos un módulo para el rol');
    return;
  }

  // Mostrar confirmación con tu card flotante
  mostrarCardConfirmacion({
    titulo: 'Confirmar herencia',
    mensaje: `¿Estás seguro de que deseas que el rol <strong>${rolNombreActual}</strong> herede permisos del rol <strong>${rolPadreNombre}</strong>?`,
    onConfirmar: function() {
      const csrfToken = $('meta[name="_csrf"]').attr('content');
debugger
      mostrarLoader("Guardando herencia...");

      $.ajax({
        url: '/portal-facil/usuarios/roles/saveInheritance',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
          rolId: parseInt(rolId),
          rolPadreId: parseInt(rolPadreId),
          modulosSeleccionados: modulosSeleccionados
        }),
        headers: {
          'X-CSRF-TOKEN': csrfToken
        },
        success: function(response) {
          cerrarLoader();
          if (response.success) {
            $('#herenciaModal').modal('hide');
            mostrarAlerta('success', 'Éxito', response.message || 'La herencia se guardó correctamente');
            cerrarCardConfirmacion();
            cerrarCardHerencia();
            //setTimeout(() => window.location.reload(), 4000);
            $(document).on('closed.bs.alert', '.alert-success', function () {
			  window.location.reload();
			});
          } else {
            mostrarAlerta('danger', 'Error', response.message || 'Ocurrió un error al guardar');
          }
        },
        error: function(xhr) {
          cerrarLoader();
          let errorMsg = 'Error al procesar la solicitud';
          if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMsg = xhr.responseJSON.message;
          }
          mostrarAlerta('danger', 'Error', errorMsg);
        }
      });
    },
    onCancelar: function() {
      cerrarCardConfirmacion();
      // opcional, puedes poner algo aquí si quieres cuando cancela
    }
  });
});


// Función para mostrar la card confirmación (reusa tu div y backdrop)
function mostrarCardConfirmacion({ titulo, mensaje, onConfirmar, onCancelar }) {
	debugger
  $('#cardPopupTitulo').html(titulo);
  $('#cardPopupTexto').html(mensaje);

  $('#cardPopupConfirmacion, #cardBackdrop').fadeIn(200);

  // Quitar eventos previos
  $('#cardPopupConfirmar').off('click');
  $('#cardPopupCancelar').off('click');
  $('#cardBackdrop').off('click');

  $('#cardPopupConfirmar').on('click', function() {
    if (typeof onConfirmar === 'function') onConfirmar();
  });

  const cerrar = () => {
    if (typeof onCancelar === 'function') onCancelar();
    cerrarCardConfirmacion();
  };

  $('#cardPopupCancelar').on('click', cerrar);
  $('#cardBackdrop').on('click', cerrar);
}

function cerrarCardConfirmacion() {
  $('#cardPopupConfirmacion, #cardBackdrop').fadeOut(200);
}

// Función para mostrar alertas tipo Bootstrap (con iconos pe-7s)
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

$('#rolPadre').on('change', function() {
	        if ($(this).val()) {
	            // Mostrar sección de módulos
	            $('#seccionModulos').slideDown();
	            
	            // Cargar árbol de módulos (sin parámetros como lo necesitas)
	            cargarArbolModulos($(this).val());
	            
	            // Habilitar botón de guardar
	            $('#guardarHerencia').prop('disabled', false);
	        } else {
	            // Ocultar sección de módulos
	            $('#seccionModulos').slideUp();
	            
	            // Deshabilitar botón de guardar
	            $('#guardarHerencia').prop('disabled', true);
	        }
});

function cargarArbolModulos(rolIdSeleccionado) {
    const $treeContainer = $('#modulosTree');
    $treeContainer.html(`
        <div class="d-flex justify-content-center align-items-center" style="height: 100px;">
            <div class="text-center text-muted">
                <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Cargando...</span>
                </div>
                <p class="mt-2">Cargando módulos...</p>
            </div>
        </div>
    `);

    // Cargar módulos disponibles
    $.ajax({
        url: '/portal-facil/usuarios/loadModulosJson',
        type: 'POST',
        dataType: 'json',
        headers: {
            [$('meta[name="_csrf-header"]').attr('content')]: $('meta[name="_csrf"]').attr('content')
        },
        success: function(modulosData) {
            if (!modulosData?.length) {
                $treeContainer.html('<div class="alert alert-info mb-0">No se encontraron módulos disponibles.</div>');
                return;
            }

            if (rolIdSeleccionado) {
                $.ajax({
                    url: '/portal-facil/usuarios/getModulosRol',
                    type: 'POST',
                    data: {
                        rolId: rolIdSeleccionado,
                        _csrf: $('meta[name="_csrf"]').attr('content')
                    },
                    dataType: 'json',
                    success: function(modulosAsignados) {
                        renderizarArbol(modulosData, modulosAsignados);
                    },
                    error: function() {
                        renderizarArbol(modulosData, []);
                    }
                });
            } else {
                renderizarArbol(modulosData, []);
            }
        },
        error: function() {
            $treeContainer.html(`
                <div class="alert alert-danger mb-0">
                    <i class="fa fa-exclamation-triangle mr-2"></i>
                    Error al cargar los módulos.
                </div>
            `);
        }
    });

  function renderizarArbol(modulosData, modulosAsignados) {
	debugger
        const modulosAsignadosIds = modulosAsignados.map(mod => mod.rom_mdl_id);

        function construirArbol(padreId, nivel = 0) {
			debugger
			//herencia
            const hijos = modulosData
                .filter(mod => mod.mdl_padre_id == padreId)
                .sort((a, b) => a.mdl_orden - b.mdl_orden);

            if (!hijos.length) return '';

            return `
                 <ul class="list-group list-group-flush ${nivel > 0 ? 'ml-3' : ''}" ${nivel > 0 ? 'style="display: none;"' : ''}>
                    ${hijos.map(mod => {
                        const tieneHijos = modulosData.some(m => m.mdl_padre_id == mod.mdl_id);
                        const estaMarcado = modulosAsignadosIds.includes(mod.mdl_id.toString());
                        const esPadre = tieneHijos;

                        return `
                            <li class="list-group-item p-2 border-0 ${esPadre ? 'has-treeview' : ''}">
                                <div class="d-flex align-items-center justify-content-between nav-link p-0">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input modulo-checkbox" id="mod_${mod.mdl_id}" data-id="${mod.mdl_id}" ${estaMarcado ? 'checked' : ''}>
                                        <label class="form-check-label" for="mod_${mod.mdl_id}">
                                            ${mod.mdl_nombre}
                                        </label>
                                    </div>
                                    ${esPadre ? '<i class="fas fa-angle-left toggle-icon"></i>' : ''}
                                </div>
                                ${esPadre ? construirArbol(mod.mdl_id, nivel + 1) : ''}
                            </li>
                        `;
                    }).join('')}
                </ul>
            `;
        }

        const idRaiz = modulosData.find(mod =>
            !modulosData.some(m => m.mdl_id === mod.mdl_padre_id)
        )?.mdl_padre_id || Math.min(...modulosData.map(mod => mod.mdl_padre_id).filter(id => id !== null));

        $treeContainer.html(construirArbol(idRaiz));
        initCustomTreeView();
    }
}

function initCustomTreeView() {
    // Manejo del toggle
    $('#modulosTree').off('click').on('click', '.has-treeview > .nav-link', function (e) {
        if ($(e.target).is('input[type="checkbox"]')) return;

        e.preventDefault();
        const $li = $(this).closest('li');
        const $submenu = $li.children('ul');
        const $icon = $(this).find('.toggle-icon');

        $li.toggleClass('open');

        if ($li.hasClass('open')) {
            $submenu.slideDown(200);
            $icon.removeClass('fa-angle-left').addClass('fa-angle-down');
        } else {
            $submenu.slideUp(200);
            $icon.removeClass('fa-angle-down').addClass('fa-angle-left');
        }
    });

    // Mostrar primer nivel
    $('#modulosTree > ul').show();

    // Checkbox: marcar hijos y padres
    $('#modulosTree').on('change', '.modulo-checkbox', function () {
        const $checkbox = $(this);
        const $li = $checkbox.closest('li');
        const isChecked = $checkbox.prop('checked');

        // Marcar hijos
        $li.find('.modulo-checkbox').prop('checked', isChecked);

        // Marcar padres
        if (isChecked) {
            $li.parents('li').each(function () {
                $(this).find('> .nav-link .modulo-checkbox').prop('checked', true);
            });
        } else {
            $li.parents('li').each(function () {
                const $parentCheckbox = $(this).find('> .nav-link .modulo-checkbox');
                const anyChildChecked = $(this).find('ul .modulo-checkbox:checked').length > 0;
                if (!anyChildChecked) {
                    $parentCheckbox.prop('checked', false);
                }
            });
        }
    });
}


function mostrarLoader() {
    $('#globalLoader').fadeIn(200);
}

function cerrarLoader() {
    $('#globalLoader').fadeOut(200);
}


//Edicion

$(document).on('click', 'tbody tr td:nth-child(2)', function(e) {
    if ($(e.target).is('button, i, a, select')) return;
    abrirCardEdicion($(this).closest('tr'));
}).css('cursor', 'pointer');

function abrirCardEdicion(row) {
    const rolId = row.find('td:first-child').text();
    const rolNombre = row.find('td:nth-child(2)').text();

    $('#editRolId').val(rolId);
    // Si tienes un input para el nombre, adaptarlo aquí. En el card no incluimos editRolNombre, pero puedes agregarlo si quieres.
    // $('#editRolNombre').val(rolNombre);

    cargarModulosParaEdicion(rolId);

    // Mostrar backdrop y card
    $('#cardBackdropEditarRol').css('display', 'block');
    $('#cardEditarRolPopup').css('display', 'block');
}


function cargarModulosParaEdicion(rolId) {
	debugger
    const $treeContainer = $('#modulosEdicionTree');
    $treeContainer.html(`
        <div class="d-flex justify-content-center align-items-center" style="height: 100px;">
            <div class="text-center">
                <i class="fas fa-spinner fa-spin fa-2x"></i>
                <p class="mt-2">Cargando módulos...</p>
            </div>
        </div>
    `);

    $.when(
        $.ajax({
            url: '/portal-facil/usuarios/loadModulosJson',
            type: 'POST',
            dataType: 'json',
            headers: {
                [$('meta[name="_csrf-header"]').attr('content')]: $('meta[name="_csrf"]').attr('content')
            }
        }),
        $.ajax({
            url: '/portal-facil/usuarios/getModulosRol',
            type: 'POST',
            data: {
                rolId: rolId,
                _csrf: $('meta[name="_csrf"]').attr('content')
            },
            dataType: 'json'
        })
    ).then(function(modulosData, modulosAsignados) {
        const modulos = modulosData[0];
        const asignados = modulosAsignados[0];

        if (!modulos?.length) {
            $treeContainer.html('<div class="alert alert-info">No se encontraron módulos</div>');
            return;
        }
debugger
        const modulosAsignadosIds = asignados.map(mod => mod.rom_mdl_id);
debugger
        function construirArbol(padreId, nivel = 0) {
			debugger
			//edicion
            const hijos = modulos
                .filter(mod => mod.mdl_padre_id == padreId)
                .sort((a, b) => a.mdl_orden - b.mdl_orden);

            if (!hijos.length) return '';
debugger
            return `
               <ul class="list-group list-group-flush ${nivel > 0 ? 'ml-3' : ''}" ${nivel > 0 ? 'style="display: none;"' : ''}>
                    ${hijos.map(mod => {
                        const tieneHijos = modulos.some(m => m.mdl_padre_id == mod.mdl_id);
                        const estaMarcado = modulosAsignadosIds.includes(mod.mdl_id.toString());
                        const esPadre = tieneHijos;
debugger
                        return `
                            <li class="list-group-item p-2 border-0 ${esPadre ? 'has-treeview' : ''}">
                                <div class="d-flex align-items-center justify-content-between nav-link p-0">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input modulo-checkbox" id="mod_${mod.mdl_id}" data-id="${mod.mdl_id}" ${estaMarcado ? 'checked' : ''}>
                                        <label class="form-check-label" for="mod_${mod.mdl_id}">
                                            ${mod.mdl_nombre}
                                        </label>
                                    </div>
                                    ${esPadre ? '<i class="fas fa-angle-left toggle-icon"></i>' : ''}
                                </div>
                                ${esPadre ? construirArbol(mod.mdl_id, nivel + 1) : ''}
                            </li>
                        `;
                    }).join('')}
                </ul>
            `;
        }
debugger
        const idRaiz = modulos.find(mod => 
            !modulos.some(m => m.mdl_id === mod.mdl_padre_id)
        )?.mdl_padre_id || Math.min(...modulos.map(mod => mod.mdl_padre_id).filter(id => id !== null));
debugger
console.log('idRaiz:', idRaiz);
console.log('Hijos encontrados:', modulos.filter(mod => mod.mdl_padre_id == idRaiz))
       // $treeContainer.html(construirArbol(idRaiz));
       const arbolHTML = construirArbol(idRaiz);
       debugger
$treeContainer.html(arbolHTML);

setTimeout(() => {
    initCustomTreeViewEdit();
}, 0);
    }).fail(function() {
        $treeContainer.html(`
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-triangle mr-2"></i>
                Error al cargar los módulos
            </div>
        `);
    });
}

function initCustomTreeViewEdit() {
    // Manejo del toggle
    
    console.log('Inicializando tree view edit...');
    console.log($('#modulosEdicionTree').find('.has-treeview .nav-link').length);

		$('#modulosEdicionTree').on('click', '.has-treeview .nav-link', function (e) {


			debugger
        if ($(e.target).is('input[type="checkbox"]')) return;
debugger
        e.preventDefault();
        const $li = $(this).closest('li');
        const $submenu = $li.children('ul');
        const $icon = $(this).find('.toggle-icon');

        $li.toggleClass('open');

        if ($li.hasClass('open')) {
			debugger
            $submenu.slideDown(200);
            $icon.removeClass('fa-angle-left').addClass('fa-angle-down');
        } else {
			debugger
            $submenu.slideUp(200);
            $icon.removeClass('fa-angle-down').addClass('fa-angle-left');
        }
    });

    // Mostrar primer nivel
    $('#modulosEdicionTree > ul').show();

    // Checkbox: marcar hijos y padres
    $('#modulosEdicionTree').on('change', '.modulo-checkbox', function () {
        const $checkbox = $(this);
        const $li = $checkbox.closest('li');
        const isChecked = $checkbox.prop('checked');

        // Marcar hijos
        $li.find('.modulo-checkbox').prop('checked', isChecked);

        // Marcar padres
        if (isChecked) {
            $li.parents('li').each(function () {
                $(this).find('> .nav-link .modulo-checkbox').prop('checked', true);
            });
        } else {
            $li.parents('li').each(function () {
                const $parentCheckbox = $(this).find('> .nav-link .modulo-checkbox');
                const anyChildChecked = $(this).find('ul .modulo-checkbox:checked').length > 0;
                if (!anyChildChecked) {
                    $parentCheckbox.prop('checked', false);
                }
            });
        }
    });
}


// Para cerrar la card con botones o backdrop
$('#cerrarCardEditarRol, #cancelarCardEditarRol, #cardBackdropEditarRol').on('click', function() {
    $('#cardBackdropEditarRol').hide();
    $('#cardEditarRolPopup').hide();
});

function mostrarCardEditarRol() {
  $('#cardBackdropEditarRol').fadeIn(0);
  $('#cardEditarRolPopup').fadeIn(200);
}

function cerrarCardEditarRol() {
  $('#cardEditarRolPopup').fadeOut(200);
  $('#cardBackdropEditarRol').fadeOut(0);
}

$('#formEditarRol').on('submit', function(e) {
	    e.preventDefault();
	    
	    const rolId = $('#editRolId').val();
	    
	    // Obtener los módulos seleccionados
	    const modulosSeleccionados = [];
	    $('#modulosEdicionTree .modulo-checkbox:checked').each(function() {
	        modulosSeleccionados.push(parseInt($(this).data('id')));
	    });
		debugger
		if (modulosSeleccionados.length === 0) {
		        mostrarAlerta('danger', 'Error', 'Debe seleccionar al menos un módulo para el rol.');
		        return;
		    }
	    debugger
	    // Primero actualizamos el nombre del rol
	    const csrfToken = $('meta[name="_csrf"]').attr('content');
	    const csrfHeader = $('meta[name="_csrf-header"]').attr('content');
	    
	    
	     actualizarModulosRol(rolId, modulosSeleccionados, csrfToken);

	});

function actualizarModulosRol(rolId, modulosSeleccionados, csrfToken) {
    $.ajax({
        url: '/portal-facil/usuarios/roles/editModulesRoles',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            rolId: rolId,
            modulosSeleccionados: modulosSeleccionados
        }),
        headers: {
            'X-CSRF-TOKEN': csrfToken
        },
        success: function(response) {
            if (response.success) {
						mostrarAlerta('success', 'Éxito', 'El rol y sus módulos se actualizaron correctamente.');
                        $('#cardEditarRolPopup').fadeOut(150);
                        $('#cardBackdropEditarRol').fadeOut(150);
                        //window.location.reload();
                        setTimeout(() => window.location.reload(), 4000);
                 
            } else {
                mostrarAlerta('danger', 'Error', response.message || 'Ocurrió un error al guardar');
            }
        },
        error: function(xhr) {
            mostrarCardConfirmacion({
                titulo: 'Error',
                mensaje: 'Error al actualizar los módulos del rol: ' + (xhr.responseJSON?.message || xhr.responseText),
                onConfirm: () => {}, // Confirmar solo cierra
                onCancel: () => {}
            });
        }
    });
}


