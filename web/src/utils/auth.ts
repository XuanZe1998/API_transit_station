const TOKEN_KEY = 'token'
const USER_KEY = 'user'
const LAST_ACTIVE_KEY = 'last_active_at'

type UserInfo = { username: string; role: string }

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function getUser(): UserInfo | null {
  const u = localStorage.getItem(USER_KEY)
  if (!u) return null
  try {
    return JSON.parse(u)
  } catch {
    return null
  }
}

export function setAuth(token: string, user: UserInfo) {
  localStorage.setItem(TOKEN_KEY, token)
  localStorage.setItem(USER_KEY, JSON.stringify(user))
  localStorage.setItem(LAST_ACTIVE_KEY, String(Date.now()))
  window.dispatchEvent(new Event('auth-changed'))
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
  window.dispatchEvent(new Event('auth-changed'))
}

export function touchActivity() {
  localStorage.setItem(LAST_ACTIVE_KEY, String(Date.now()))
}

export function initInactivityGuard(timeoutMs: number, onTimeout: () => void) {
  let timer: number | null = null
  const reset = () => {
    touchActivity()
    if (timer) window.clearTimeout(timer)
    timer = window.setTimeout(() => {
      clearAuth()
      window.dispatchEvent(new Event('auth-timeout'))
      onTimeout()
    }, timeoutMs)
  }
  const events = ['click', 'mousemove', 'keydown', 'scroll', 'touchstart', 'visibilitychange']
  events.forEach(e => window.addEventListener(e, reset, { passive: true }))
  reset()
  return () => {
    if (timer) window.clearTimeout(timer)
    events.forEach(e => window.removeEventListener(e, reset))
  }
}

