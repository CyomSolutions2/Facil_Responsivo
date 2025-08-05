//Buscar Rol
function setAccion(valor) {
    document.getElementById("accion").value = valor;
}

//$(document).on('dblclick', 'tbody tr td:nth-child(2)', function(e) {
	$(document).on('click', 'tbody tr td:nth-child(2)', function(e) {
    if ($(e.target).is('button, i, a, select')) return;

    const row = $(this).closest('tr');
    const rolId = row.find('td:first-child').text();
    const rolNombre = $(this).text();
debugger
    abrirCardEdicionReportes(rolId, rolNombre);
}).css('cursor', 'pointer');




function abrirCardEdicionReportes(rolId, rolNombre) {
    // Oculta cualquier otro popup/card abierto
    $('.floating-card').hide();

    // Llena los datos
    $('#editReporteId').val(rolId);
    $('#cardEditarReportes .card-header h5').text(`Editar Reportes para ${rolNombre}`);

    // Mostrar el card
    $('#cardEditarReportes').fadeIn();

    // Cargar los reportes
    cargarReportesParaEdicion(rolId);
}

function cargarReportesParaEdicion(rolId) {
	debugger
    const $container = $('#reportesEdicionTree');
    $container.html(`
        <div class="d-flex justify-content-center align-items-center" style="height: 100px;">
            <div class="text-center">
                <i class="fas fa-spinner fa-spin fa-2x"></i>
                <p class="mt-2">Cargando reportes...</p>
            </div>
        </div>
    `);

    // Cargar todos los tipos de reportes disponibles
    $.ajax({
        url: '/portal-facil/usuarios/loadTiposReportesJson',
        type: 'POST',
        dataType: 'json',
        headers: {
            [$('meta[name="_csrf-header"]').attr('content')]: $('meta[name="_csrf"]').attr('content')
        },
        success: function(tiposReportes) {
            // Cargar los reportes asignados al rol
            $.ajax({
                url: '/portal-facil/usuarios/getReportesRol',
                type: 'POST',
                data: {
                    rolId: rolId,
                    _csrf: $('meta[name="_csrf"]').attr('content')
                },
                dataType: 'json',
                success: function(reportesAsignados) {
                    renderizarReportes(tiposReportes, reportesAsignados);
                },
                error: function() {
                    renderizarReportes(tiposReportes, []);
                }
            });
        },
        error: function() {
            $container.html(`
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle mr-2"></i>
                    Error al cargar los tipos de reportes
                </div>
            `);
        }
    });

    function renderizarReportes(tiposReportes, reportesAsignados) {
        // Extraer IDs de reportes asignados
        const reportesAsignadosIds = reportesAsignados.map(reporte => 
            reporte.idTipoReporte || reporte.mdl_tipo_id || reporte.rom_mdl_id
        );
        
        // Crear lista de checkboxes
        let html = '<div class="list-group">';
        
        tiposReportes.forEach(reporte => {
            const reporteId = reporte.mdl_tipo_id || reporte.idTipoReporte;
            const reporteNombre = reporte.mdl_tipo_reporte || reporte.tipoReporte;
            const estaAsignado = reportesAsignadosIds.includes(reporteId.toString());
            
            html += `
                <div class="list-group-item">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input reporte-checkbox" 
                               id="reporte_${reporteId}" 
                               data-id="${reporteId}"
                               ${estaAsignado ? 'checked' : ''}>
                        <label class="custom-control-label" for="reporte_${reporteId}">
                            ${reporteNombre}
                        </label>
                    </div>
                </div>
            `;
        });
        
        html += '</div>';
        $container.html(html);
    }
}

$('#formEditarReportes').on('submit', function(e) {
    e.preventDefault();
    
    const rolId = $('#editReporteId').val();
    const reportesSeleccionados = [];
    
    $('.reporte-checkbox:checked').each(function() {
        reportesSeleccionados.push($(this).data('id'));
    });
    
    if (reportesSeleccionados.length === 0) {
        mostrarAlerta('danger', 'Error', 'Debe seleccionar al menos un reporte para el rol');
        return;
    }
    
    const csrfToken = $('meta[name="_csrf"]').attr('content');
    
    $.ajax({
        url: '/portal-facil/usuarios/updateAssignReports',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            rolId: parseInt(rolId),
            reportesSeleccionados: reportesSeleccionados
        }),
        headers: {
            'X-CSRF-TOKEN': csrfToken
        },
        success: function(response) {
            if (response.success) {
               
					 mostrarAlerta('success', 'Éxito', response.message || 'Los reportes se actualizaron correctamente');
                    // Cierra el card flotante en lugar del modal
                    $('#cardEditarReportes').fadeOut();
                
            } else {
                mostrarAlerta('danger', 'Error', response.message || 'Error al actualizar los reportes');
            }
        },
        error: function(xhr) {            
             mostrarAlerta('danger', 'Error', 'Error al actualizar los reportes: ' + (xhr.responseJSON?.message || xhr.responseText));
        }
    });
});

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
