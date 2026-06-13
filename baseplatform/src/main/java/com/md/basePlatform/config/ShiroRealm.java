package com.md.basePlatform.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
/**
 * Shiro 认证/授权 Realm
 *
 * <p>负责用户身份认证和权限授权的核心逻辑。当前为开发阶段使用内存中的固定用户，
 * 后续可扩展为从数据库加载用户和权限信息。
 *
 * <p>注意：此类的 Bean 由 {@link ShiroConfig#shiroRealm()} 手动注册，
 * 不要添加 @Component 等自动扫描注解，否则会造成 Bean 名称冲突。
 */
public class ShiroRealm extends AuthorizingRealm {

    /**
     * 授权：根据用户身份加载其角色和权限
     *
     * <p>调用时机：每次需要权限判断时（如 @RequiresRoles、hasRole() 调用），
     * Shiro 会调用此方法获取用户的角色和权限集合并缓存。
     *
     * <p>权限字符串格式：资源:操作
     * <ul>
     *   <li>"*:*" — 通配符，表示所有资源的所有操作（超级管理员）</li>
     *   <li>"drone:view" — 表示只能查看无人机（drone 资源的 view 操作）</li>
     *   <li>"drone:*" — 表示 drone 资源的所有操作（CRUD）</li>
     * </ul>
     *
     * @param principals 用户身份集合（来自认证时保存的 SimpleAuthenticationInfo）
     * @return 包含角色和权限字符串的 AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 从认证信息中取出当前用户名（认证时存入的第一个参数）
        String username = (String) principals.getPrimaryPrincipal();
        // 根据用户名赋予不同的角色和权限
        if ("admin".equals(username)) {
            info.addRole("admin");                     // admin 角色
            info.addStringPermission("*:*");           // 所有权限（超级管理员）
        } else {
            info.addRole("user");                      // 普通用户角色
            info.addStringPermission("drone:view");    // 只有无人机查看权限
        }
        return info;
    }

    /**
     * 认证：校验用户名密码
     *
     * <p>调用时机：当用户调用 Subject.login(token) 时，Shiro 会将 token 传给此方法。
     *
     * <p>认证流程：
     * <ol>
     *   <li>从 token 中提取用户名</li>
     *   <li>根据用户名查询用户信息（当前为内存固定用户，后续改数据库查询）</li>
     *   <li>将查到的用户信息封装为 AuthenticationInfo 返回</li>
     *   <li>Shiro 框架会自动比对 token 中的密码和 AuthenticationInfo 中的密码</li>
     *   <li>密码不匹配 → 框架抛出 IncorrectCredentialsException</li>
     *   <li>用户不存在 → 本方法抛出 AuthenticationException</li>
     * </ol>
     *
     * <p>注意：本方法只负责返回正确的用户信息，密码比对由 Shiro 框架的 Authenticator 完成。
     * SimpleAuthenticationInfo 的最后一个参数 getName() 返回 Realm 名称，用于区分多 Realm 场景。
     *
     * @param token 包含用户提交的用户名和密码
     * @return 认证信息（正确密码 + 用户名）
     * @throws AuthenticationException 当用户名不存在时
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 将通用的 AuthenticationToken 转为 Shiro 的用户名密码令牌
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // 校验用户是否存在（开发阶段使用固定账号）
        // 生产环境应改为：从数据库根据 username 查询，查不到则抛异常
        if (!"admin".equals(username) && !"user".equals(username)) {
            throw new AuthenticationException("用户不存在: " + username);
        }

        // 开发阶段固定密码（生产环境应从数据库读取加密后的密码）
        String password = "admin".equals(username) ? "admin123" : "user123";

        // 返回认证信息：
        // 参数1：username 作为 principal（用户身份标识）
        // 参数2：正确的密码（Shiro 会与 token 中的密码比对）
        // 参数3：当前 Realm 名称（通过 getName() 获取，用于多 Realm 场景识别来源）
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
