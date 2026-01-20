window.alert = function(message, title, callback) {
  if (title === undefined) {
    title = 'ITSM';
  }
  console.log(message);

  message = message.replace(/\n/g, '<br/>'); // 행바꿈 제거
  message = message.replace(/\r/g, '<br/>');

  console.log(message);

  if ($("#bootstrap-alert-box-modal").length === 0) {
    $("body").append('<div id="bootstrap-alert-box-modal" class="modal fade">\
      <div class="modal-dialog modal-dialog-centered">\
        <div class="modal-content">\
          <div class="modal-header">\
            <h4 class="modal-title"></h4>\
            <button type="button" class="btn modal-close" data-dismiss="modal"  aria-label="창닫기"></button>\
          </div>\
          <div class="modal-body"><p class="pd30"></p></div>\
            <div class="modal-footer">\
              <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="ico-cancel"></i>창닫기</button>\
            </div>\
          </div>\
        </div>\
      </div>');
    }

  $("#bootstrap-alert-box-modal .modal-header h4").text(title || "");
  $("#bootstrap-alert-box-modal .modal-body p").html(message || "");
  $("#bootstrap-alert-box-modal").modal('show');
  $("#bootstrap-alert-box-modal").off("hidden.bs.modal");
  if (callback && typeof callback === "function") {
    $("#bootstrap-alert-box-modal").on("hidden.bs.modal", callback);
  }
};

window.confirm = function(message, title, yes_label, callback) {
  $("#bootstrap-confirm-box-modal").data('confirm-yes', false);
  
  if ($("#bootstrap-confirm-box-modal").length === 0) {
    $("body").append('<div id="bootstrap-confirm-box-modal" class="modal fade">\
      <div class="modal-dialog modal-dialog-centered">\
        <div class="modal-content">\
          <div class="modal-header">\
            <h4 class="modal-title"></h4>\
            <button type="button" class="btn modal-close" data-dismiss="modal"  aria-label="창닫기"></button>\
          </div>\
          <div class="modal-body"><p class="pd30"></p></div>\
            <div class="modal-footer">\
              <button type="button" class="btn btn-primary">' + (yes_label || '확인') + '</button>\
              <button type="button" data-dismiss="modal" aria-label="창닫기" class="btn btn-secondary">취소</button>\
            </div>\
          </div>\
        </div>\
      </div>');

    $("#bootstrap-confirm-box-modal .modal-footer .btn-primary").on('click', function() {
      $("#bootstrap-confirm-box-modal").data('confirm-yes', true);
      $("#bootstrap-confirm-box-modal").modal('hide');
      return false;
    });
    $("#bootstrap-confirm-box-modal").on('hide.bs.modal', function() {
      console.log($("#bootstrap-confirm-box-modal").data('confirm-yes'));
      if (callback) {
        callback($("#bootstrap-confirm-box-modal").data('confirm-yes'));
      }
    });
  }

  $("#bootstrap-confirm-box-modal .modal-header h4").text(title || "");
  $("#bootstrap-confirm-box-modal .modal-body p").html(message || "");
  $("#bootstrap-confirm-box-modal").modal('show');
};