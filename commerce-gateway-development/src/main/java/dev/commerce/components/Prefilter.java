package dev.commerce.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class Prefilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("------ PREFILTER ------");
        filterChain.doFilter(request, response);

    }
    // class này mục đích để mỗi request đi vào hệ thống đều phải qua lớp này trước khi vào controller
    // để sau này có thể thêm các logic xử lý chung cho tất cả request ở đây
    // ví dụ như logging, kiểm tra header, kiểm tra token, v.v.
    // nếu fail ở đây thì sẽ không cho request đi tiếp vào controller
    // và trả về lỗi ngay từ đây
    // hoặc có thể tạo 1 authentication entry point riêng để xử lý các lỗi liên quan đến authentication
    // và authorization ta sẽ tạo class entry point ở package dev.commerce.components
    // và cấu hình nó trong lớp AppConfig
}
