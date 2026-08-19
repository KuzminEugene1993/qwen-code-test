import { useMemo, useState } from 'react'
import './styles.css'

function App() {
  const [loading, setLoading] = useState(false)
  const [status, setStatus] = useState('')
  const [statusType, setStatusType] = useState('')

  const [newUser, setNewUser] = useState({ email: '', name: '' })
  const [users, setUsers] = useState([])
  const [selectedUserId, setSelectedUserId] = useState('')

  const [profileForm, setProfileForm] = useState({ bio: '', age: '', city: '' })
  const [profile, setProfile] = useState(null)

  const [likeForm, setLikeForm] = useState({ fromUserId: '', toUserId: '' })
  const [matches, setMatches] = useState([])

  const selectedUser = useMemo(
    () => users.find((u) => String(u.id) === String(selectedUserId)) || null,
    [users, selectedUserId],
  )

  function showStatus(message, type = '') {
    setStatus(message)
    setStatusType(type)
  }

  async function api(path, options = {}) {
    const res = await fetch(path, {
      headers: { 'Content-Type': 'application/json' },
      ...options,
    })

    if (!res.ok) {
      let details = ''
      try {
        details = await res.text()
      } catch {
        details = ''
      }
      throw new Error(`HTTP ${res.status}${details ? `: ${details}` : ''}`)
    }

    if (res.status === 204) return null
    return res.json()
  }

  async function handleCreateUser(e) {
    e.preventDefault()
    if (!newUser.email || !newUser.name) {
      showStatus('Заполни email и имя', 'bad')
      return
    }

    setLoading(true)
    try {
      const created = await api('/api/users', {
        method: 'POST',
        body: JSON.stringify(newUser),
      })

      setNewUser({ email: '', name: '' })
      setSelectedUserId(String(created.id))
      showStatus(`Пользователь ${created.name} создан (id=${created.id})`, 'good')
      await loadUsers()
      await loadProfile(created.id)
    } catch (err) {
      showStatus(`Ошибка создания пользователя: ${err.message}`, 'bad')
    } finally {
      setLoading(false)
    }
  }

  async function loadUsers() {
    setLoading(true)
    try {
      const data = await api('/api/users')
      setUsers(data)
      showStatus(`Загружено пользователей: ${data.length}`, 'good')
    } catch (err) {
      showStatus(`Ошибка загрузки пользователей: ${err.message}`, 'bad')
    } finally {
      setLoading(false)
    }
  }

  async function loadProfile(userIdValue) {
    const userId = userIdValue || selectedUserId
    if (!userId) {
      showStatus('Сначала выбери пользователя', 'bad')
      return
    }

    setLoading(true)
    try {
      const data = await api(`/api/profiles/${userId}`)
      setProfile(data)
      setProfileForm({
        bio: data.bio || '',
        age: data.age ?? '',
        city: data.city || '',
      })
      showStatus(`Профиль пользователя #${userId} загружен`, 'good')
    } catch (err) {
      showStatus(`Ошибка загрузки профиля: ${err.message}`, 'bad')
    } finally {
      setLoading(false)
    }
  }

  async function handleSaveProfile(e) {
    e.preventDefault()
    if (!selectedUserId) {
      showStatus('Сначала выбери пользователя', 'bad')
      return
    }

    setLoading(true)
    try {
      const payload = {
        bio: profileForm.bio,
        age: profileForm.age === '' ? null : Number(profileForm.age),
        city: profileForm.city,
      }
      const data = await api(`/api/profiles/${selectedUserId}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      })
      setProfile(data)
      showStatus('Профиль сохранён', 'good')
    } catch (err) {
      showStatus(`Ошибка сохранения профиля: ${err.message}`, 'bad')
    } finally {
      setLoading(false)
    }
  }

  async function handleLike(e) {
    e.preventDefault()
    if (!likeForm.fromUserId || !likeForm.toUserId) {
      showStatus('Укажи fromUserId и toUserId', 'bad')
      return
    }

    setLoading(true)
    try {
      const data = await api('/api/likes', {
        method: 'POST',
        body: JSON.stringify({
          fromUserId: Number(likeForm.fromUserId),
          toUserId: Number(likeForm.toUserId),
        }),
      })
      showStatus(data.match ? 'Это MATCH! 💘' : 'Лайк отправлен', 'good')
    } catch (err) {
      showStatus(`Ошибка лайка: ${err.message}`, 'bad')
    } finally {
      setLoading(false)
    }
  }

  async function handleLoadMatches() {
    const userId = selectedUserId || likeForm.fromUserId
    if (!userId) {
      showStatus('Выбери пользователя для загрузки мэтчей', 'bad')
      return
    }

    setLoading(true)
    try {
      const data = await api(`/api/matches/${userId}`)
      setMatches(data)
      showStatus(`Найдено мэтчей: ${data.length}`, 'good')
    } catch (err) {
      showStatus(`Ошибка загрузки мэтчей: ${err.message}`, 'bad')
    } finally {
      setLoading(false)
    }
  }

  const avatarLetter = selectedUser?.name?.trim()?.[0]?.toUpperCase() || '❤'

  return (
    <div className="app-shell">
      <section className="hero">
        <div className="badge">
          <span className="dot" />
          frontend online
        </div>
        <h1>Dating App</h1>
        <p>Красивый демо-фронт с кликабельными кнопками и страницей профиля пользователя.</p>
      </section>

      <div className="layout">
        <section className="card">
          <h2>Пользователи</h2>

          <form className="form-grid" onSubmit={handleCreateUser}>
            <input
              className="input"
              placeholder="Email"
              value={newUser.email}
              onChange={(e) => setNewUser((p) => ({ ...p, email: e.target.value }))}
            />
            <input
              className="input"
              placeholder="Имя"
              value={newUser.name}
              onChange={(e) => setNewUser((p) => ({ ...p, name: e.target.value }))}
            />
            <div className="btn-row">
              <button className="btn" type="submit" disabled={loading}>Создать пользователя</button>
              <button className="btn secondary" type="button" onClick={loadUsers} disabled={loading}>Обновить список</button>
            </div>
          </form>

          <div className="user-list">
            {users.map((u) => (
              <button
                key={u.id}
                className="item"
                style={{ textAlign: 'left', cursor: 'pointer' }}
                onClick={() => {
                  setSelectedUserId(String(u.id))
                  loadProfile(u.id)
                }}
              >
                <strong>#{u.id} {u.name}</strong>
                <div className="small">{u.email}</div>
              </button>
            ))}
            {users.length === 0 && <div className="small">Пока нет пользователей</div>}
          </div>
        </section>

        <section className="card profile-card">
          <h2>Профиль пользователя</h2>

          <div className="profile-top">
            <div className="avatar">{avatarLetter}</div>
            <div>
              <h3 className="profile-name">{selectedUser?.name || 'Не выбран'}</h3>
              <div className="profile-meta">{selectedUser?.email || 'Выбери пользователя слева'}</div>
            </div>
          </div>

          <p className="profile-bio">
            {profile?.bio?.trim()
              ? profile.bio
              : 'Здесь будет описание профиля. Заполни форму ниже и сохрани.'}
          </p>

          <form className="form-grid" onSubmit={handleSaveProfile} style={{ marginTop: 14 }}>
            <input
              className="input"
              placeholder="Город"
              value={profileForm.city}
              onChange={(e) => setProfileForm((p) => ({ ...p, city: e.target.value }))}
            />
            <input
              className="input"
              type="number"
              min="18"
              max="100"
              placeholder="Возраст"
              value={profileForm.age}
              onChange={(e) => setProfileForm((p) => ({ ...p, age: e.target.value }))}
            />
            <textarea
              className="input"
              rows={4}
              placeholder="О себе"
              value={profileForm.bio}
              onChange={(e) => setProfileForm((p) => ({ ...p, bio: e.target.value }))}
            />
            <div className="btn-row">
              <button className="btn purple" type="submit" disabled={loading}>Сохранить профиль</button>
              <button className="btn secondary" type="button" onClick={() => loadProfile()} disabled={loading}>Обновить профиль</button>
            </div>
          </form>
        </section>

        <section className="card">
          <h2>Лайки</h2>
          <form className="form-grid" onSubmit={handleLike}>
            <input
              className="input"
              type="number"
              placeholder="fromUserId"
              value={likeForm.fromUserId}
              onChange={(e) => setLikeForm((p) => ({ ...p, fromUserId: e.target.value }))}
            />
            <input
              className="input"
              type="number"
              placeholder="toUserId"
              value={likeForm.toUserId}
              onChange={(e) => setLikeForm((p) => ({ ...p, toUserId: e.target.value }))}
            />
            <div className="btn-row">
              <button className="btn" type="submit" disabled={loading}>Поставить лайк</button>
              <button className="btn secondary" type="button" onClick={handleLoadMatches} disabled={loading}>Показать мэтчи</button>
            </div>
          </form>
        </section>

        <section className="card">
          <h2>Мэтчи</h2>
          <div className="match-list">
            {matches.map((m, i) => (
              <div key={`${m.userA}-${m.userB}-${i}`} className="item">
                <strong>Пара:</strong> #{m.userA} ❤️ #{m.userB}
              </div>
            ))}
            {matches.length === 0 && <div className="small">Пока нет мэтчей</div>}
          </div>
        </section>
      </div>

      <div className={`status ${statusType === 'good' ? 'good' : ''} ${statusType === 'bad' ? 'bad' : ''}`}>
        {status || 'Готово к работе. Сначала создай пользователей.'}
      </div>
    </div>
  )
}

export default App
