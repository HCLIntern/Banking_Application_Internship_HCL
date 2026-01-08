export default function AccountCard({account}) {
  return (
    <div style={{border:'1px solid #eee', padding:10, borderRadius:6}}>
      <h4>{account?.name || 'Account Name'}</h4>
      <p>Balance: {account?.balance ?? '0.00'}</p>
    </div>
  );
}
