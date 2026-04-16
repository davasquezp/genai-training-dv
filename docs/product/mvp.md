# MVP scope

## MVP goal
Validate that dancers will register interest and that organizers can get actionable insights.

## In scope (MVP)
- Public landing page + Interest form
- Capture: name, role (lead/follower), country, styles (multi-select)
- Persist registrations (currently in-memory; next step is durable storage)
- Admin/debug listing endpoint (`GET /api/dancers`) for verification
- Communities (concept + initial slice)
  - A community has a **name**
  - A community is based in a particular **region** (city or area)
  - A community has **one or more admins**
  - A dancer can be associated with **zero or many** communities
  - Tiered limits define how many dancers can belong to a community
  - A community has a **landing page**
  - A community can be associated with related features such as **Events**, **Parties**, **Schools**, or **Instructors**
  - A community can publish several links for communication channels:
    - WhatsApp
    - Email
    - Telegram
    - Instagram
- Basic deployability:
  - backend on Render
  - frontend on Cloudflare Pages

## Non-goals (MVP)
- Authentication / accounts
- Matching algorithm
- Messaging / chat
- Payments
- Event scheduling

## Release criteria
- A dancer can submit from the deployed Pages site and the backend accepts it without CORS errors
- Submissions appear via `GET /api/dancers`
- Swagger loads in production for debugging

