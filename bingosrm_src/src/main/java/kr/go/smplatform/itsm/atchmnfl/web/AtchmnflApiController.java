package kr.go.smplatform.itsm.atchmnfl.web;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;

@RestController
@RequestMapping("/api/v1/attachments")
public class AtchmnflApiController {
    private final AtchmnflService atchmnflService;

    public AtchmnflApiController(AtchmnflService atchmnflService) {
        this.atchmnflService = atchmnflService;
    }

    @PostMapping
    public Map<String, Object> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "atchmnflId", required = false) String atchmnflId) throws Exception {
        AtchmnflVO saved = atchmnflId == null || atchmnflId.isEmpty()
                ? atchmnflService.saveFile(file)
                : atchmnflService.saveFile(file, atchmnflId);

        List<AtchmnflVO> list = atchmnflService.retrieveList(saved);
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("atchmnflId", saved.getAtchmnflId());
        response.put("resultList", list);
        return response;
    }

    @GetMapping("/{atchmnflId}")
    public List<AtchmnflVO> list(@PathVariable("atchmnflId") String atchmnflId) throws Exception {
        AtchmnflVO param = new AtchmnflVO();
        param.setAtchmnflId(atchmnflId);
        return atchmnflService.retrieveList(param);
    }

    @GetMapping("/{atchmnflId}/{atchmnflSn}")
    public ResponseEntity<FileSystemResource> download(
            @PathVariable("atchmnflId") String atchmnflId,
            @PathVariable("atchmnflSn") String atchmnflSn) throws Exception {
        AtchmnflVO param = new AtchmnflVO();
        param.setAtchmnflId(atchmnflId);
        param.setAtchmnflSn(atchmnflSn);

        AtchmnflVO found = atchmnflService.retrieve(param);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(found.getStreAllCours());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(file.toPath());
        MediaType mediaType = contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + found.getOrginlFileNm() + "\"")
                .body(new FileSystemResource(file));
    }

    @DeleteMapping("/{atchmnflId}/{atchmnflSn}")
    public Map<String, Object> delete(
            @PathVariable("atchmnflId") String atchmnflId,
            @PathVariable("atchmnflSn") String atchmnflSn) throws Exception {
        AtchmnflVO param = new AtchmnflVO();
        param.setAtchmnflId(atchmnflId);
        param.setAtchmnflSn(atchmnflSn);

        atchmnflService.delete(param);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("deleted", true);
        return response;
    }
}
